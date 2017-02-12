package es.humarbean.gespagos.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.List;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.database.MateDataSource;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateListFragment extends Fragment {

    private static int SIMPLE_MODE = 1;
    private static int PRO_MODE = 2;

    private RecyclerView mCrimeRecyclerView;
    private MateAdapter mAdapter;

    private MateDataSource mMateDS;
    private int mCalculationMode = PRO_MODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mMateDS = MateDataSource.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mate_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
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
                Intent intent = MateEditorActivity.newIntent(getActivity());
                startActivity(intent);
                return true;
            case R.id.menu_item_reset_group:
                MateDataSource ds = MateDataSource.getInstance(getActivity());
                ds.resetGroup();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_calculation_mode:
                if (mCalculationMode == PRO_MODE) {
                    mCalculationMode = SIMPLE_MODE;
                } else {
                    mCalculationMode = PRO_MODE;
                }
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new MateAdapter();
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private String composeText(int numPayments, int numRounds) {
        boolean DEBUG = false;
        String valRet = "";
        if (DEBUG) {
            valRet += numPayments + " / " + numRounds + " = ";
        }
        if (numRounds > 0) {
            valRet += (numPayments * 100 / numRounds) + "%";
        } else {
            valRet += "0 %";
        }
        return valRet;
    }

    private float composeColor(int numPayments, int numRounds) {
        if (numRounds > 0) {
            return 1 - (float) numPayments / (float) numRounds;
        }
        return 1;
    }

    private class MateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Integer mMateId;
        private ToggleButton mMateButton;
        private Button mPaymentButton;
        private Button mPaymentButtonPro;

        public MateHolder(View itemView) {
            super(itemView);

            mMateButton = (ToggleButton) itemView.findViewById(R.id.list_item_mate_button_mate);
            mMateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    mMateDS.getMateById(mMateId).setSelected(checked);
                    if (!mCrimeRecyclerView.isComputingLayout()) {
                        mAdapter.notifyItemChanged(mMateDS.getMatePositionById(mMateId));
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
            mPaymentButton.setOnClickListener(new PaymentButtonListener(mAdapter));

            mPaymentButtonPro = (Button) itemView.findViewById(R.id.list_item_mate_button_payment_pro);
            mPaymentButtonPro.setOnClickListener(new PaymentButtonListener(mAdapter));
        }

        int getColor(double value) {
            return android.graphics.Color.HSVToColor(new float[]{(float) value * 120f, 1f, 1f});
        }

        class PaymentButtonListener implements View.OnClickListener {
            private MateAdapter mAdapter;

            PaymentButtonListener(MateAdapter adapter) {
                mAdapter = adapter;
            }

            @Override
            public void onClick(View v) {
                mMateDS.resetPayerFlag();
                mMateDS.getMateById(mMateId).setPayer(true);
                processPayment();
                mAdapter.notifyDataSetChanged();
            }
        }

        private void processPayment() {
            List<Mate> mates = mMateDS.getMates();

            /* Num mates in this round. */
            int numMatesInRound = 0;
            for (Mate mate : mates) {
                if (mate.isSelected()) {
                    numMatesInRound++;
                }
            }
            for (Mate mate : mates) {
                if (mate.isSelected()) {
                    /* This mate in this round. */
                    mate.setNumRounds(mate.getNumRounds() + 1);
                    mate.setNumMatesInRounds(mate.getNumMatesInRounds() + numMatesInRound);
                    if (mate.isPayer()) {
                        /* This mate is payer. */
                        mate.setNumPayments(mate.getNumPayments() + 1);
                        mate.setNumMatesInPayments(mate.getNumMatesInPayments() + numMatesInRound);
                    }
                    mMateDS.persistMate(mate);
                }
            }
        }

        public void bindMate(Mate mate) {
            mMateId = mate.getId();

            mMateButton.setTextOn(mate.getName());
            mMateButton.setTextOff(mate.getName());
            mMateButton.setChecked(mate.isSelected());

            mPaymentButton.setText(composeText(mate.getNumPayments(), mate.getNumRounds()));
            mPaymentButton.setEnabled(mate.isSelected());
            if (mate.isSelected()) {
                float colorValue = composeColor(mate.getNumPayments(), mate.getNumRounds());
                int color = getColor(colorValue);
                mPaymentButton.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mPaymentButton.setTextColor(Color.BLACK);
            } else {
                mPaymentButton.getBackground().clearColorFilter();
                mPaymentButtonPro.setTextColor(Color.GRAY);
            }
            if (SIMPLE_MODE != mCalculationMode) {
                mPaymentButton.setVisibility(View.GONE);
            } else {
                mPaymentButton.setVisibility(View.VISIBLE);
            }

            mPaymentButtonPro.setText(composeText(mate.getNumMatesInPayments(), mate.getNumMatesInRounds()));
            mPaymentButtonPro.setEnabled(mate.isSelected());
            if (mate.isSelected()) {
                float colorValue = composeColor(mate.getNumMatesInPayments(), mate.getNumMatesInRounds());
                int color = getColor(colorValue);
                mPaymentButtonPro.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mPaymentButtonPro.setTextColor(Color.BLACK);
            } else {
                mPaymentButtonPro.getBackground().clearColorFilter();
                mPaymentButtonPro.setTextColor(Color.GRAY);
            }
            if (PRO_MODE != mCalculationMode) {
                mPaymentButtonPro.setVisibility(View.GONE);
            } else {
                mPaymentButtonPro.setVisibility(View.VISIBLE);
            }
        }

        private void prepareButton(Button button, Mate mate) {
            button.setText(composeText(mate.getNumMatesInPayments(), mate.getNumMatesInRounds()));
            button.setEnabled(mate.isSelected());
            if (mate.isSelected()) {
                float colorValue = composeColor(mate.getNumMatesInPayments(), mate.getNumMatesInRounds());
                int color = getColor(colorValue);
                button.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                button.setTextColor(Color.BLACK);
            } else {
                button.getBackground().clearColorFilter();
                button.setTextColor(Color.GRAY);
            }
            if (PRO_MODE != mCalculationMode) {
                button.setVisibility(View.GONE);
            } else {
                button.setVisibility(View.VISIBLE);
            }
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
            Mate mate = mMateDS.getMateInPosition(position);
            holder.bindMate(mate);
        }

        @Override
        public int getItemCount() {
            return mMateDS.getListSize();
        }
    }
}