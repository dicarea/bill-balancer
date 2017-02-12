package es.humarbean.gespagos.application;

import android.app.Application;
import android.content.Context;

public class GesPagosApp extends Application {

    private static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

}