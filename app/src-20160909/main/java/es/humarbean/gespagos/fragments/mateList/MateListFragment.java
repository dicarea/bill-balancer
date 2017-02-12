package es.humarbean.gespagos.fragments.mateList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.AndroidDatabaseManager;
import es.humarbean.gespagos.views.GroupEditorActivity;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;
    private MateAdapter mAdapter;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;

    private Group mGroup;

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

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.mate_list_layout);

        mDrawerList = (ListView) view.findViewById(R.id.mate_list_left_menu);
        mDrawerList.setAdapter(new DrawerListAdapter<Group>(getActivity(), R.layout.drawer_list_item,
                helper.getGroups()));
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGroup = (Group) parent.getItemAtPosition(position);
                mDrawerList.setItemChecked(position, true);
                mDrawerLayout.closeDrawer(mDrawerList);
                getActivity().setTitle(mGroup.getName());
                helper.loadMates(mGroup.getId());
                mAdapter.notifyDataSetChanged();
            }
        });
        mDrawerList.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mGroup = (Group) parent.getItemAtPosition(position);
                mDrawerList.setItemChecked(position, true);
                Intent intent = GroupEditorActivity.newIntentEdit(getActivity(), mGroup.getId());
                getActivity().startActivity(intent);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        helper.getGroups();
        mDrawerList.setAdapter(new DrawerListAdapter<Group>(getActivity(), R.layout.drawer_list_item,
                helper.getGroups()));
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
                startActivity(MateEditorActivity.newIntentCreate(getActivity(), mGroup.getId()));
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

    private class DrawerListAdapter<T> extends ArrayAdapter {

        public DrawerListAdapter(Context context, int resource, List<T> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.drawer_list_item, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.drawer_item_text);
            Group item = (Group) getItem(position);
            name.setText(item.getName());
            return convertView;
        }
    }

    private class MateAdapter extends RecyclerView.Adapter<MateHolder> {

        @Override
        public MateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_mate, parent, false);
            return new MateHolder(view, getActivity(), mCrimeRecyclerView);
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