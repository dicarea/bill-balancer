package es.humarbean.gespagos.views;

import android.support.v4.app.Fragment;

import es.humarbean.gespagos.fragments.MateListFragment;
import es.humarbean.gespagos.views.commons.SingleFragmentActivity;

public class MateListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new MateListFragment();
    }
}
