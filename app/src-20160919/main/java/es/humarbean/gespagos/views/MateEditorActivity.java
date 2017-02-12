package es.humarbean.gespagos.views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import es.humarbean.gespagos.fragments.MateEditorFragment;
import es.humarbean.gespagos.views.commons.SingleFragmentActivity;

public class MateEditorActivity extends SingleFragmentActivity {

    public static final String EXTRA_MATE_ID = "es.humarbean.gespagos.idCrime";
    public static final String EXTRA_GROUP_ID = "es.humarbean.gespagos.idGroup";

    public static Intent newIntentCreate(Context packageContext, Integer idGroup) {
        Intent intent = new Intent(packageContext, MateEditorActivity.class);
        intent.putExtra(EXTRA_GROUP_ID, idGroup);
        return intent;
    }

    public static Intent newIntentEdit(Context packageContext, Integer idMate) {
        Intent intent = new Intent(packageContext, MateEditorActivity.class);
        intent.putExtra(EXTRA_MATE_ID, idMate);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new MateEditorFragment();
    }
}
