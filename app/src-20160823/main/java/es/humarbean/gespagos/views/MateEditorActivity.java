package es.humarbean.gespagos.views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import es.humarbean.gespagos.fragments.MateEditorFragment;
import es.humarbean.gespagos.views.commons.SingleFragmentActivity;

public class MateEditorActivity extends SingleFragmentActivity {

    public static final String EXTRA_MATE_ID = "es.humarbean.gespagos.crime_id";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MateEditorActivity.class);
        return intent;
    }

    public static Intent newIntent(Context packageContext, Integer id) {
        Intent intent = new Intent(packageContext, MateEditorActivity.class);
        intent.putExtra(EXTRA_MATE_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new MateEditorFragment();
    }
}
