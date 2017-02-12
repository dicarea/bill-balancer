package es.humarbean.gespagos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.database.GesPagosDataSource;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.views.GroupEditorActivity;

public class GroupEditorFragment extends Fragment {
    private static final String ARG_GROUP_ID = "group_id";

    private Group mGroup;

    private EditText mGroupName;

    public static GroupEditorFragment newInstance() {
        GroupEditorFragment fragment = new GroupEditorFragment();
        return fragment;
    }

    public static GroupEditorFragment newInstance(Integer id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUP_ID, id);
        GroupEditorFragment fragment = new GroupEditorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        /* Recupera el elemento o crea uno nuevo. */
        Integer id = (Integer) getActivity().getIntent().getSerializableExtra(GroupEditorActivity.EXTRA_GROUP_ID);

        if (id == null) {
            /* Creando. */
            mGroup = new Group();
        } else {
            /* Editando. */
            GesPagosDataSource ds = GesPagosDataSource.getInstance();
            mGroup = ds.getGroupById(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_editor, container, false);
        mGroupName = (EditText) view.findViewById(R.id.group_editor_name);
        mGroupName.setText(mGroup.getName());

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_group_editor, menu);
        if (mGroup.getId() == null) {
            menu.findItem(R.id.menu_item_delete_group).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_group:
                mGroup.setName(mGroupName.getText().toString());
                GesPagosDataSource.getInstance().persistGroup(mGroup);

                Intent returnIntent = getActivity().getIntent();
                returnIntent.putExtra("idGroup", mGroup.getId());
                returnIntent.putExtra("groupName", mGroup.getName());
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}