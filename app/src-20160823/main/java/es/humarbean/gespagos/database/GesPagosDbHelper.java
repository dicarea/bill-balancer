package es.humarbean.gespagos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.humarbean.gespagos.database.GesPagosDbSchema.MateTable;

public class GesPagosDbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "gesPagos.db";

    public GesPagosDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MateTable.NAME + "(" +
                MateTable.Cols.ID + " integer primary key autoincrement, " +
                MateTable.Cols.NAME + ", " +
                MateTable.Cols.NUM_PAYMENTS + ", " +
                MateTable.Cols.NUM_ROUNDS +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
