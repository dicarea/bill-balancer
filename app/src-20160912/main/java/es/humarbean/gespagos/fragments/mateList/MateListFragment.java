package es.humarbean.gespagos.fragments.mateList;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import java.util.List;

import es.humarbean.gespagos.R;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.views.AndroidDatabaseManager;
import es.humarbean.gespagos.views.GroupEditorActivity;
import es.humarbean.gespagos.views.MateEditorActivity;

public class MateListFragment extends Fragment {

    private static int MENU_GROUP_GROUPS = 33;

    private RecyclerView mCrimeRecyclerView;
    private MateAdapter mAdapter;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private SubMenu mSubMenuGroups;

    private Group mGroup;

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
        mGroup = helper.getGroups().get(0);
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
        mCrimeRecyclerView.setAdapter(mAdapter = new MateAdapter());

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.mate_list_layout);

        mNavigationView = (NavigationView) view.findViewById(R.id.mate_list_left_menu);

        mSubMenuGroups = mNavigationView.getMenu().findItem(R.id.drawer_groups_item).getSubMenu();

        mDrawerToggle = new ActionBarDrawerToggle(this.getActivity(), mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (menuItem.getGroupId() == MENU_GROUP_GROUPS) {
                    /* Si es de la lista de grupos. */
                    /* Desmarcar anterior y marcar nuevo. */
                    mSubMenuGroups.findItem(mGroup.getId()).setChecked(false);
                    menuItem.setChecked(true);

                    /* Se guarda el grupo seleccionado. */
                    mGroup = helper.getGroupById(menuItem.getItemId());

                    /* Se cierra el drawer. */
                    mDrawerLayout.closeDrawer(mNavigationView);

                    /* Se carga la lista de mates. */
                    helper.loadMates(mGroup.getId());
                    mAdapter.notifyDataSetChanged();

                    /* Se indica en el grupo en el titulo. */
                    getActivity().setTitle(mGroup.getName());
                } else {
                    /* Si no es de la lista de grupos. */
                    switch (menuItem.getItemId()) {
                        case R.id.drawer_add_group:
                            /* Boton de añadir grupo. */
                            Intent intent = GroupEditorActivity.newIntentEdit(getActivity(), null);
                            getActivity().startActivity(intent);
                            break;
                        case R.id.drawer_settings:
                            Intent dbManager = new Intent(getActivity(), AndroidDatabaseManager.class);
                            startActivity(dbManager);
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        /* Se recargan las opciones de menu del drawer. */
        mSubMenuGroups.clear();
        List<Group> listGroup = helper.getGroups();
        for (Group group : listGroup) {
            mSubMenuGroups.add(MENU_GROUP_GROUPS, group.getId(), Menu.NONE, group.getName());
        }

        /* Se comprueba si se ha borrado el grupo seleccionado. */
        if (mGroup != null && helper.getGroupById(mGroup.getId()) == null) {
            mGroup = null;
            helper.loadMates(null);
        }

        /* Se actualiza la lista de mates. */
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
                startActivity(MateEditorActivity.newIntentCreate(getActivity(), mGroup.getId()));
                return true;
            case R.id.menu_item_reset_group:
                helper.resetGroup();
                mAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_item_log:
                helper.printRounds();
                return true;
            case R.id.group_edit:
                /* Editar o borrar grupo que esta seleccionado. */
                Intent intent = GroupEditorActivity.newIntentEdit(getActivity(), mGroup.getId());
                getActivity().startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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