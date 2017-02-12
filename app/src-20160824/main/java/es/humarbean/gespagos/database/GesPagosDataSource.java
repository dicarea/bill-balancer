package es.humarbean.gespagos.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.humarbean.gespagos.application.GesPagosApp;
import es.humarbean.gespagos.database.GesPagosDbSchema.MateTable;
import es.humarbean.gespagos.database.GesPagosDbSchema.PaymentTable;
import es.humarbean.gespagos.database.GesPagosDbSchema.RoundTable;
import es.humarbean.gespagos.database.cursor.MateCursorWrapper;
import es.humarbean.gespagos.database.cursor.PaymentCursorWrapper;
import es.humarbean.gespagos.database.cursor.RoundCursorWrapper;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.models.Payment;
import es.humarbean.gespagos.models.Round;

public class GesPagosDataSource {
    private static GesPagosDataSource sInstance;

    private SQLiteDatabase mDatabase;

    private List<Mate> mMates;

    private GesPagosDataSource() {
        mDatabase = new GesPagosDbHelper(GesPagosApp.getAppContext()).getWritableDatabase();
        loadMates();
    }

    public static GesPagosDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new GesPagosDataSource();
        }
        return sInstance;
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

    public void resetAllPayersFlag() {
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

    public int getMatePositionById(Integer id) {
        return mMates.indexOf(getMateById(id));
    }

    public void deleteMate(Mate mate) {
        deleteMateFromDB(mate.getId());
        mMates.remove(mMates.indexOf(mate));
    }

    public void resetGroup() {
        for (Mate mate : mMates) {
            mate.resetValues();
            updateMate(mate);
        }
    }

    //*************************
    // MATE TABLE
    //*************************

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

    //*************************
    // ROUND TABLE
    //*************************

    private static ContentValues getContentValues(Round round) {
        ContentValues values = new ContentValues();
        values.put(RoundTable.Cols.ID, round.getId());
        values.put(RoundTable.Cols.DATE, round.getDate() != null ? round.getDate().getTime() : null);
        values.put(RoundTable.Cols.PLACE, round.getPlace());
        return values;
    }

    public Round addRound(Round round) {
        ContentValues values = getContentValues(round);
        long id = mDatabase.insert(RoundTable.NAME, null, values);
        round.setId(((int) id));
        return round;
    }

    private RoundCursorWrapper queryRounds(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RoundTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new RoundCursorWrapper(cursor);
    }

    public List<Round> getRounds() {
        List<Round> rounds = new ArrayList<>();
        RoundCursorWrapper cursor = queryRounds(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                rounds.add(cursor.getRound());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return rounds;
    }

    //*************************
    // PAYMENT TABLE
    //*************************

    private static ContentValues getContentValues(Payment payment) {
        ContentValues values = new ContentValues();
        values.put(PaymentTable.Cols.ID, payment.getId());
        values.put(PaymentTable.Cols.ID_ROUND, payment.getIdRound());
        values.put(PaymentTable.Cols.ID_MATE, payment.getIdMate());
        values.put(PaymentTable.Cols.NUM_ITEMS, payment.getNumItems());
        values.put(PaymentTable.Cols.IS_PAYER, payment.isPayer());
        return values;
    }

    public Payment addPayment(Payment payment) {
        ContentValues values = getContentValues(payment);
        long id = mDatabase.insert(PaymentTable.NAME, null, values);
        payment.setId(((int) id));
        return payment;
    }

    private PaymentCursorWrapper queryPayment(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PaymentTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new PaymentCursorWrapper(cursor);
    }

    public List<Payment> getPayments(Integer idRound) {
        List<Payment> payments = new ArrayList<>();
        PaymentCursorWrapper cursor = queryPayment(PaymentTable.Cols.ID_ROUND + " = " +
                idRound.toString(), null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                payments.add(cursor.getPayment());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return payments;
    }

}
