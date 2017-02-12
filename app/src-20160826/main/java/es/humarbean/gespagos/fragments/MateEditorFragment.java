package es.humarbean.gespagos.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
                GesPagosDataSource.getInstance().persistMate(mMate);
                Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.deleted, Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

}