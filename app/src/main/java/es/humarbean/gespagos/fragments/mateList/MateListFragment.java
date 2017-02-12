package es.humarbean.gespagos.fragments.mateList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.database.GesPagosDataSource;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.AndroidDatabaseManager;
import es.humarbean.gespagos.views.GroupEditorActivity;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateListFragment extends Fragment {

    private static int MENU_GROUP_SELECTION = 33;

    private RecyclerView mCrimeRecyclerView;
    private MateAdapter mAdapter;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private SubMenu mSubMenuGroups;

    private MateListHelper helper;
    private MateListViewHelper helperView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* La toolbar tendra opciones. */
        setHasOptionsMenu(true);

        /* Dependency Injection. */
        helper = new MateListHelper();
        helperView = new MateListViewHelper(getActivity());

        /* Grupo por defecto (ultimo). */
        helper.setGroup(helper.getSelectedGroup());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mate_list, container, false);

        /* Se crea la toolbar. */
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.mate_list_recyclerview);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCrimeRecyclerView.setAdapter(mAdapter = new MateAdapter(this));
        mCrimeRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.mate_list_layout);

        mNavigationView = (NavigationView) view.findViewById(R.id.mate_list_left_menu);

        mSubMenuGroups = mNavigationView.getMenu().findItem(R.id.drawer_groups_item).getSubMenu();

        mDrawerToggle = new ActionBarDrawerToggle(this.getActivity(), mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getGroupId() == MENU_GROUP_SELECTION) {
                    /* Se guarda el grupo seleccionado. */
                    helper.setGroup(helper.getGroupById(menuItem.getItemId()));
                    helper.selectGroup(helper.getGroup());

                    /* Se actualiza la vista de mates segun grupo seleccionado. */
                    updateMateList();

                    /* Se actualiza el dawer. */
                    updateDrawerList();
                } else {
                    switch (menuItem.getItemId()) {
                        case R.id.drawer_add_group:
                            /* Boton de añadir grupo. */
                            Intent intent = GroupEditorActivity.newIntentEdit(getActivity(), null);
                            startActivityForResult(intent, GroupEditorActivity.REQ_CODE_ADD);
                            break;
                        case R.id.drawer_settings:
                            Intent dbManager = new Intent(getActivity(), AndroidDatabaseManager.class);
                            startActivity(dbManager);
                            break;
                        default:
                            break;
                    }
                }
                /* Se cierra el drawer. */
                mDrawerLayout.closeDrawer(mNavigationView);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        /* Se recargan las opciones de menu del drawer. */
        updateDrawerList();

        /* Se actualiza la vista de mates segun grupo seleccionado. */
        updateMateList();
    }

    private void updateMateList() {

        if (helper.getGroup() != null) {
            /* Se carga la lista de mates. */
            helper.loadMates(helper.getGroup());
            /* Se indica en el grupo en el titulo. */
            getActivity().setTitle(helper.getGroup().getName());
        } else {
            helper.loadMates(null);
            getActivity().setTitle("");
        }
        mAdapter.notifyDataSetChanged();

    }

    private void updateDrawerList() {
        /* Se recargan las opciones de menu del drawer. */
        mSubMenuGroups.clear();
        List<Group> listGroup = helper.getGroups();
        for (Group group : listGroup) {
            mSubMenuGroups.add(MENU_GROUP_SELECTION, group.getId(), Menu.NONE, group.getName())
                    .setIcon(R.drawable.group);
        }
        if (helper.getGroup() != null) {
            /* Seleccionado. */
            mSubMenuGroups.findItem(helper.getGroup().getId()).setChecked(true);
        }

        /* Repintamos menu de toolbar para ocultar o mostrar. */
        ((AppCompatActivity) getActivity()).getSupportActionBar().invalidateOptionsMenu();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (helper.getGroup() == null) {
            menu.setGroupVisible(R.id.grp2, false);
        } else {
            menu.setGroupVisible(R.id.grp2, true);
        }
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
                startActivityForResult(MateEditorActivity.newIntentCreate(getActivity(), helper.getGroup().getId()), 44);
                return true;
            case R.id.menu_item_reset_group:
                confirmReset();
                return true;
            case R.id.menu_item_log:

                Intent intentSend = new Intent(Intent.ACTION_SEND);
                intentSend.setType("message/rfc822");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                intentSend.putExtra(Intent.EXTRA_SUBJECT, "Payments report at " + sdf.format(new Date()));

                String separator = System.getProperty("line.separator");
                List<String> report = GesPagosDataSource.getInstance().generateGroupReport(helper.getGroup());
                StringBuilder body = new StringBuilder();
                for (int i = 0; i < report.size(); i++) {
                    body.append(report.get(i)).append(separator);
                    /* Liberar memoria. */
                    report.set(i, null);
                }
                System.gc();

                intentSend.putExtra(Intent.EXTRA_TEXT, body.toString());
                try {
                    startActivity(Intent.createChooser(intentSend, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.group_edit:
                /* Editar grupo seleccionado. */
                Intent intent = GroupEditorActivity.newIntentEdit(getActivity(), helper.getGroup().getId());
                startActivityForResult(intent, GroupEditorActivity.REQ_CODE_UPDATE);
                return true;
            case R.id.group_delete:
                confirmDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GroupEditorActivity.REQ_CODE_ADD) {
                helper.setGroup(new Group());
                helper.getGroup().setId(data.getIntExtra("idGroup", 0));
                helper.getGroup().setName(data.getStringExtra("groupName"));
                helper.selectGroup(helper.getGroup());
                Snackbar.make(getView(), R.string.created, Snackbar.LENGTH_LONG).show();
            } else if (requestCode == GroupEditorActivity.REQ_CODE_UPDATE) {
                helper.getGroup().setName(data.getStringExtra("groupName"));
                Snackbar.make(getView(), R.string.saved, Snackbar.LENGTH_LONG).show();
            } else if (requestCode == 44) {
                if (data.getIntExtra("operation", 0) == 1) {
                    Snackbar.make(getView(), R.string.created, Snackbar.LENGTH_LONG).show();
                } else if (data.getIntExtra("operation", 0) == 2) {
                    Snackbar.make(getView(), R.string.saved, Snackbar.LENGTH_LONG).show();
                } else if (data.getIntExtra("operation", 0) == 3) {
                    Snackbar.make(getView(), R.string.deleted, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm_delete)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.deleteGroup(helper.getGroup());
                        helper.setGroup(null);
                        updateDrawerList();
                        updateMateList();
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        Snackbar.make(getView(), R.string.deleted, Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    private void confirmReset() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.confirm_reset)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper.resetGroup(helper.getGroup());
                        updateMateList();
                        Snackbar.make(getView(), R.string.reset_done, Snackbar.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create().show();
    }

    private class MateAdapter extends RecyclerView.Adapter<MateHolder> {

        private Fragment fragment;

        public MateAdapter(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public MateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_mate, parent, false);
            return new MateHolder(view, fragment, mCrimeRecyclerView);
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