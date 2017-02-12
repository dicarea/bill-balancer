package es.humarbean.gespagos.views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import es.humarbean.gespagos.fragments.GroupEditorFragment;
import es.humarbean.gespagos.views.commons.SingleFragmentActivity;

public class GroupEditorActivity extends SingleFragmentActivity {

    public static final String EXTRA_GROUP_ID = "es.humarbean.gespagos.idGroup";

    public static Intent newIntentEdit(Context packageContext, Integer idGroup) {
        Intent intent = new Intent(packageContext, GroupEditorActivity.class);
        intent.putExtra(EXTRA_GROUP_ID, idGroup);
        return intent;
    }

    public static Intent newIntentCreate(Context packageContext) {
        Intent intent = new Intent(packageContext, GroupEditorActivity.class);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return new GroupEditorFragment();
    }
}
