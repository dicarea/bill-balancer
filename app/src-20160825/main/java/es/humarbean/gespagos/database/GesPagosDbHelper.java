package es.humarbean.gespagos.database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import es.humarbean.gespagos.database.GesPagosDbSchema.MateTable;
import es.humarbean.gespagos.database.GesPagosDbSchema.PaymentTable;
import es.humarbean.gespagos.database.GesPagosDbSchema.RoundTable;

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

        db.execSQL("create table " + RoundTable.NAME + "(" +
                RoundTable.Cols.ID + " integer primary key autoincrement, " +
                RoundTable.Cols.DATE + ", " +
                RoundTable.Cols.PLACE + ", " +
                RoundTable.Cols.DELETED +
                ")"
        );

        db.execSQL("create table " + PaymentTable.NAME + "(" +
                PaymentTable.Cols.ID + " integer primary key autoincrement, " +
                PaymentTable.Cols.ID_ROUND + ", " +
                PaymentTable.Cols.ID_MATE + ", " +
                PaymentTable.Cols.NUM_ITEMS + ", " +
                PaymentTable.Cols.IS_PAYER +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}
