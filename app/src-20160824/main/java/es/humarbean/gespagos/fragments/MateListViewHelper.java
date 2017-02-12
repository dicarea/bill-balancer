package es.humarbean.gespagos.fragments;

import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class MateListViewHelper {
    private static boolean DEBUG_MODE = true;
    private FragmentActivity mActivity;

    MateListViewHelper(FragmentActivity activity) {
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

    public float composeColor(int numPayments, int numRounds) {
        if (numRounds > 0) {
            return 1 - (float) numPayments / (float) numRounds;
        }
        return 1;
    }

}
