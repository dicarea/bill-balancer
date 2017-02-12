package es.humarbean.gespagos.fragments.mateList;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.widget.Toast;

import es.humarbean.gespagos.models.MateStats;

public class MateListViewHelper {
    private static boolean DEBUG_MODE = true;
    private Activity mActivity;

    MateListViewHelper(Activity activity) {
        mActivity = activity;
    }

    public void openToast(@StringRes int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public String composeText(int numPayments, int numRounds) {
        String valRet = "";
        if (DEBUG_MODE) {
            valRet += numPayments + " / " + numRounds + " = ";
        }
        if (numRounds > 0) {
            valRet += (numPayments * 100 / numRounds) + "%";
        } else {
            valRet += "0 %";
        }
        return valRet;
    }

    int getColor(double value) {
        return android.graphics.Color.HSVToColor(new float[]{(float) value * 120f, 1f, 1f});
    }

    public float composeColor(MateStats stats) {
        if (stats.getNumRows() > 0) {
            return 1 - (float) stats.getNumPayments() / (float) stats.getNumPayments();
        }
        return 1;
    }


}
