package es.humarbean.gespagos.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.AndroidDatabaseManager;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private MateAdapter mAdapter;
    private ListView mDrawerList;

    private MateListHelper helper;
    private MateListViewHelper helperView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        /* Dependency Injection. */
        helper = new MateListHelper();
        helperView = new MateListViewHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mate_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.mate_list_recyclerview);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimeRecyclerView.setAdapter(mAdapter = new MateAdapter());

        mDrawerList = (ListView) view.findViewById(R.id.mate_list_left_menu);
        mDrawerList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.drawer_list_item, new String[]{"Uno", "Dos"}));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mate_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_mate:
                startActivity(MateEditorActivity.newIntent(getActivity()));
                return true;
            case R.id.menu_item_reset_group:
                helper.resetGroup();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_calculation_mode:
                //helper.changeCalculationMode();
                //mAdapter.notifyDataSetChanged();

                Intent dbmanager = new Intent(getActivity(), AndroidDatabaseManager.class);
                startActivity(dbmanager);

                return true;
            case R.id.menu_item_log:
                helper.printRounds();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Integer mMateId;
        private ToggleButton mMateButton;
        private Button mPaymentButton;
        private ToggleButton mMultiplierButton;

        public MateHolder(View itemView) {
            super(itemView);

            mMateButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_mate);
            mMateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    helper.markMateSelection(mMateId, checked);
                    if (!mCrimeRecyclerView.isComputingLayout()) {
                        mAdapter.notifyItemChanged(helper.getMatePositionById(mMateId));
                    }
                }
            });
            mMateButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = MateEditorActivity.newIntent(getActivity(), mMateId);
                    startActivity(intent);
                    return true;
                }
            });

            mPaymentButton = (Button) itemView.findViewById(R.id.list_item_mate_button_payment);
            mPaymentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.markMateAsPayer(mMateId);
                    helper.insertRoundAndPayments();
                    helperView.openToast(R.string.process_payment);
                    mAdapter.notifyDataSetChanged();
                }
            });

            mMultiplierButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_multiplier);
            mMultiplierButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    helper.markMultiplier(mMateId, checked);
                }
            });
            mMultiplierButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    helper.incrementMultiplier(mMateId);
                    mAdapter.notifyItemChanged(helper.getMatePositionById(mMateId));
                    return true;
                }
            });

        }

        public void bindMate(Mate mate) {
            mMateId = mate.getId();

            mMateButton.setTextOn(mate.getName());
            mMateButton.setTextOff(mate.getName());
            mMateButton.setChecked(mate.isSelected());

            mPaymentButton.setText(String.valueOf(mate.getStats().getWeight()));
            mPaymentButton.setEnabled(mate.isSelected());
            if (mate.isSelected()) {
                //float colorValue = helperView.composeColor(numPayments, numRounds);
                //int color = helperView.getColor(colorValue);
                //mPaymentButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mPaymentButton.setTextColor(Color.BLACK);
            } else {
                mPaymentButton.getBackground().clearColorFilter();
                mPaymentButton.setTextColor(Color.GRAY);
            }

            mMultiplierButton.setEnabled(mate.isSelected());
            mMultiplierButton.setChecked(mate.ismMultiplierActive());
            mMultiplierButton.setText("X" + mate.getMultiplier());
            mMultiplierButton.setTextOn("X" + mate.getMultiplier());
            mMultiplierButton.setTextOff("X" + mate.getMultiplier());

        }

        @Override
        public void onClick(View v) {
            // EMPTY
        }
    }

    private class MateAdapter extends RecyclerView.Adapter<MateHolder> {

        @Override
        public MateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_mate, parent, false);
            return new MateHolder(view);
        }

        @Override
        public void onBindViewHolder(MateHolder holder, int position) {
            Mate mate = helper.getMateInPosition(position);
            holder.bindMate(mate);
        }

        @Override
        public int getItemCount() {
            return helper.getNumMates();
        }
    }
}