package es.humarbean.gespagos.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateEditorFragment extends Fragment {
    private static final String ARG_MATE_ID = "crime_id";

    private Mate mMate;

    private EditText mMateName;

    public static MateEditorFragment newInstance() {
        MateEditorFragment fragment = new MateEditorFragment();
        return fragment;
    }

    public static MateEditorFragment newInstance(Integer id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MATE_ID, id);
        MateEditorFragment fragment = new MateEditorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        /* Recupera el elemento o crea uno nuevo. */
        Integer id = (Integer) getActivity().getIntent().getSerializableExtra(MateEditorActivity.EXTRA_MATE_ID);

        if (id == null) {
            /* Creando. */
            mMate = new Mate();
            Integer idGroup = (Integer) getActivity().getIntent().getSerializableExtra(MateEditorActivity.EXTRA_GROUP_ID);
            mMate.setIdGroup(idGroup);
        } else {
            /* Editando. */
            GesPagosDataSource ds = GesPagosDataSource.getInstance();
            mMate = ds.getMateById(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mate_editor, container, false);
        mMateName = (EditText) view.findViewById(R.id.mate_editor_name);
        mMateName.setText(mMate.getName());

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_mate_editor, menu);
        if (mMate.getId() == null) {
            menu.findItem(R.id.menu_item_delete_mate).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_save_mate:
                mMate.setName(mMateName.getText().toString());
                boolean creating = mMate.getId() == null;
                GesPagosDataSource.getInstance().persistMate(mMate);
                Intent returnIntent = getActivity().getIntent();
                returnIntent.putExtra("operation", creating ? 1 : 2);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
                return true;
            case R.id.menu_item_delete_mate:
                confirmDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm_delete)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GesPagosDataSource.getInstance().deleteMate(mMate);
                        Snackbar.make(getView(), R.string.deleted, Snackbar.LENGTH_LONG).show();
                        Intent returnIntent = getActivity().getIntent();
                        returnIntent.putExtra("operation", 3);
                        getActivity().setResult(Activity.RESULT_OK, returnIntent);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

}