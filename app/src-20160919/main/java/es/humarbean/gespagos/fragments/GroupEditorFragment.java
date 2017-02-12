package es.humarbean.gespagos.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.database.GesPagosDataSource;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.views.GroupEditorActivity;

public class GroupEditorFragment extends Fragment {
    private static final String ARG_GROUP_ID = "group_id";

    private Group mGroup;

    private TextInputLayout mGroupName;

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
        mGroupName = (TextInputLayout) view.findViewById(R.id.group_editor_name);
        mGroupName.getEditText().setText(mGroup.getName());

        mGroupName.getEditText().addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mGroupName.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        /* Abre el teclado al entrar. */
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_group:

                /* Validaciones. */
                if (mGroupName.getEditText().getText() == null ||
                        mGroupName.getEditText().getText().toString().equals("")) {
                    mGroupName.setError("Required");
                    return false;
                }
                if (mGroupName.getEditText().getText().length() > mGroupName.getCounterMaxLength()) {
                    mGroupName.setError("Error: max length " + mGroupName.getCounterMaxLength());
                    return false;
                }

                mGroup.setName(mGroupName.getEditText().getText().toString());
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