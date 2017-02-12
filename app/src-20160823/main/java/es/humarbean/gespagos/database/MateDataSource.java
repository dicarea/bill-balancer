package es.humarbean.gespagos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.humarbean.gespagos.database.GesPagosDbSchema.MateTable;
import es.humarbean.gespagos.models.Mate;

public class MateDataSource {
    private static MateDataSource sInstance;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    private List<Mate> mMates;

    private MateDataSource(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new GesPagosDbHelper(mContext).getWritableDatabase();
        loadMates();
    }

    public static MateDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MateDataSource(context);
        }
        return sInstance;
    }

    private static ContentValues getContentValues(Mate mate) {
        ContentValues values = new ContentValues();
        values.put(MateTable.Cols.ID, mate.getId());
        values.put(MateTable.Cols.NAME, mate.getName());
        values.put(MateTable.Cols.NUM_PAYMENTS, mate.getNumPayments());
        values.put(MateTable.Cols.NUM_ROUNDS, mate.getNumRounds());
        return values;
    }

    private void addMate(Mate mate) {
        ContentValues values = getContentValues(mate);
        long id = mDatabase.insert(MateTable.NAME, null, values);
        mate.setId(((int) id));
        mMates.add(mate);
    }

    private void updateMate(Mate mate) {
        ContentValues values = getContentValues(mate);
        mDatabase.update(MateTable.NAME, values, MateTable.Cols.ID + " = " + mate.getId(), null);
    }

    private MateCursorWrapper queryMates(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MateTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new MateCursorWrapper(cursor);
    }

    private List<Mate> getMatesFromDB() {
        List<Mate> mates = new ArrayList<>();
        MateCursorWrapper cursor = queryMates(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mates.add(cursor.getMate());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return mates;
    }

    private Mate getMateFromDB(Integer id) {
        MateCursorWrapper cursor = queryMates(
                MateTable.Cols.ID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getMate();
        } finally {
            cursor.close();
        }
    }

    private void deleteMateFromDB(Integer id) {
        mDatabase.delete(MateTable.NAME, MateTable.Cols.ID + " = ?", new String[]{id.toString()});
    }

    public void loadMates() {
        mMates = getMatesFromDB();
    }

    public Mate getMateInPosition(int pos) {
        return mMates.get(pos);
    }

    public int getListSize() {
        return mMates.size();
    }

    public List<Mate> getMates() {
        return mMates;
    }

    public Mate getMateById(Integer id) {
        for (Mate mate : mMates) {
            if (mate.getId().equals(id)) {
                return mate;
            }
        }
        return null;
    }

    public void resetPayerFlag() {
        for (Mate mate : mMates) {
            mate.setPayer(false);
        }
    }

    public void persistMate(Mate mate) {
        if (mate.getId() == null) {
            addMate(mate);
        } else {
            updateMate(mate);
        }
    }

    public int getMatePositionById(int id) {
        return mMates.indexOf(getMateById(id));
    }

    public void deleteMate(Mate mate) {
        deleteMateFromDB(mate.getId());
        mMates.remove(mMates.indexOf(mate));
    }

    public void resetGroup() {
        for (Mate mate : mMates) {
            mate.setNumPayments(0);
            mate.setNumRounds(0);
            mate.setNumMatesInPayments(0);
            mate.setNumMatesInRounds(0);
            updateMate(mate);
        }
    }
}
