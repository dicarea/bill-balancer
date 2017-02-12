package es.humarbean.gespagos.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.humarbean.gespagos.application.GesPagosApp;
import es.humarbean.gespagos.database.GesPagosDbSchema.GroupTable;
import es.humarbean.gespagos.database.GesPagosDbSchema.MateTable;
import es.humarbean.gespagos.database.GesPagosDbSchema.PaymentTable;
import es.humarbean.gespagos.database.GesPagosDbSchema.RoundTable;
import es.humarbean.gespagos.database.cursor.GroupCursorWrapper;
import es.humarbean.gespagos.database.cursor.MateCursorWrapper;
import es.humarbean.gespagos.database.cursor.PaymentCursorWrapper;
import es.humarbean.gespagos.database.cursor.RoundCursorWrapper;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.models.Payment;
import es.humarbean.gespagos.models.Round;

public class GesPagosDataSource {
    private static GesPagosDataSource sInstance;

    private SQLiteDatabase mDatabase;

    private List<Mate> mMates = new ArrayList<>();
    private List<Group> mGroups = new ArrayList<>();
    private Group mGroup;

    private GesPagosDataSource() {
        mDatabase = new GesPagosDbHelper(GesPagosApp.getAppContext()).getWritableDatabase();
        loadGroups();
    }

    public static GesPagosDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new GesPagosDataSource();
        }
        return sInstance;
    }

    private static ContentValues getContentValues(Mate mate) {
        ContentValues values = new ContentValues();
        values.put(MateTable.Cols.ID, mate.getId());
        values.put(MateTable.Cols.NAME, mate.getName());
        values.put(MateTable.Cols.ID_GROUP, mate.getIdGroup());
        values.put(MateTable.Cols.SELECTED, mate.isSelected());
        return values;
    }

    private static ContentValues getContentValues(Group group) {
        ContentValues values = new ContentValues();
        values.put(GroupTable.Cols.ID, group.getId());
        values.put(GroupTable.Cols.NAME, group.getName());
        values.put(GroupTable.Cols.SELECTED, group.isSelected());
        return values;
    }

    private static ContentValues getContentValues(Round round) {
        ContentValues values = new ContentValues();
        values.put(RoundTable.Cols.ID, round.getId());
        values.put(RoundTable.Cols.ID_GROUP, round.getIdGroup());
        values.put(RoundTable.Cols.DATE, round.getDate() != null ? round.getDate().getTime() : null);
        values.put(RoundTable.Cols.PLACE, round.getPlace());
        return values;
    }

    private static ContentValues getContentValues(Payment payment) {
        ContentValues values = new ContentValues();
        values.put(PaymentTable.Cols.ID, payment.getId());
        values.put(PaymentTable.Cols.ID_GROUP, payment.getIdGroup());
        values.put(PaymentTable.Cols.ID_ROUND, payment.getIdRound());
        values.put(PaymentTable.Cols.ID_MATE, payment.getIdMate());
        values.put(PaymentTable.Cols.NUM_ITEMS, payment.getNumItems());
        values.put(PaymentTable.Cols.IS_PAYER, payment.isPayer());
        return values;
    }

    public void loadMates(Group group) {
        if (group != null) {
            mMates = getMatesByGroup(group);
            loadMateStats();
        } else {
            mMates.clear();
        }
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

    //*************************
    // MATE TABLE
    //*************************

    public int getMatePositionById(Integer id) {


        return mMates.indexOf(getMateById(id));
    }


    private void addMate(Mate mate) {
        ContentValues values = getContentValues(mate);
        long id = mDatabase.insert(MateTable.NAME, null, values);
        mate.setId(((int) id));
        mMates.add(mate);
    }

    public void deleteMate(Mate mate) {
        mDatabase.delete(MateTable.NAME, MateTable.Cols.ID + " = ?", new String[]{mate.getId().toString()});
        mMates.remove(mMates.indexOf(mate));
    }

    private void updateMate(Mate mate) {
        ContentValues values = getContentValues(mate);
        mDatabase.update(MateTable.NAME, values, MateTable.Cols.ID + " = " + mate.getId(), null);
    }

    public void resetGroup() {
        for (Mate mate : mMates) {
            mate.resetValues();
            updateMate(mate);
        }
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

    private List<Mate> getMatesByGroup(Group group) {
        List<Mate> mates = new ArrayList<>();
        MateCursorWrapper cursor = queryMates(MateTable.Cols.ID_GROUP + "=" + group.getId(), null);
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

    //*************************
    // ROUND TABLE
    //*************************

    public Round addRound(Round round) {
        ContentValues values = getContentValues(round);
        long id = mDatabase.insert(RoundTable.NAME, null, values);
        round.setId(((int) id));
        return round;
    }

    public void deleteRound(Round round) {
        mDatabase.delete(RoundTable.NAME, RoundTable.Cols.ID + " = ?", new String[]{round.getId().toString()});
    }

    private void deleteRoundsByGroup(Group group) {
        mDatabase.delete(RoundTable.NAME, RoundTable.Cols.ID_GROUP + " = " + group.getId().toString(), null);
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

    public List<Round> getRoundsByGroup(Group group) {
        List<Round> rounds = new ArrayList<>();
        RoundCursorWrapper cursor = queryRounds(RoundTable.Cols.ID_GROUP + " = " +
                group.getId().toString(), null);
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

    public Payment addPayment(Payment payment) {
        ContentValues values = getContentValues(payment);
        long id = mDatabase.insert(PaymentTable.NAME, null, values);
        payment.setId(((int) id));
        return payment;
    }

    public void deletePayment(Payment payment) {
        mDatabase.delete(PaymentTable.NAME, PaymentTable.Cols.ID + " = ?", new String[]{payment.getId().toString()});
    }

    public void deletePaymentsByMate(Mate mate) {
        mDatabase.delete(PaymentTable.NAME, PaymentTable.Cols.ID_MATE + " = " + mate.getId().toString(), null);
    }

    private void deletePaymentsByGroup(Group group) {
        mDatabase.delete(PaymentTable.NAME, PaymentTable.Cols.ID_GROUP + " = " + group.getId().toString(), null);
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

    //*************************
    // GROUP TABLE
    //*************************

    public Group addGroup(Group group) {
        ContentValues values = getContentValues(group);
        long id = mDatabase.insert(GroupTable.NAME, null, values);
        group.setId(((int) id));
        return group;
    }

    private void deleteGroupById(Integer id) {
        mDatabase.delete(GroupTable.NAME, GroupTable.Cols.ID + " = ?", new String[]{id.toString()});
    }

    private void updateGroup(Group group) {
        ContentValues values = getContentValues(group);
        mDatabase.update(GroupTable.NAME, values, GroupTable.Cols.ID + " = " + group.getId(), null);
    }

    public List<Group> getGroups() {
        loadGroups();
        return mGroups;
    }

    public void loadGroups() {
        List<Group> groups = new ArrayList<>();
        GroupCursorWrapper cursor = queryGroup(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                groups.add(cursor.getGroup());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        mGroups = groups;
    }

    public Group getGroupById(Integer id) {
        GroupCursorWrapper cursor = queryGroup(GroupTable.Cols.ID + " = " + id.toString(), null);
        try {
            cursor.moveToFirst();
            while (cursor.getCount() == 1) {
                return cursor.getGroup();
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private GroupCursorWrapper queryGroup(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                GroupTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new GroupCursorWrapper(cursor);
    }

    public void persistGroup(Group group) {
        if (group.getId() == null) {
            addGroup(group);
        } else {
            updateGroup(group);
        }
    }

    public void setSelectedGroup(Group group) {
        List<Group> groups = this.getGroups();
        for (Group groupItem : groups) {
            if (group.getId().equals(groupItem.getId())) {
                if (!groupItem.isSelected()) {
                    groupItem.setSelected(true);
                    updateGroup(groupItem);
                }
            } else {
                if (groupItem.isSelected()) {
                    groupItem.setSelected(false);
                    updateGroup(groupItem);
                }
            }
        }
    }

    public Group getSelectedGroup() {
        List<Group> groups = this.getGroups();
        for (Group group : groups) {
            if (group.isSelected()) {
                return group;
            }
        }
        return null;
    }

    //*************************
    // TRANSACTIONS
    //*************************

    public void deleteGroup(Group group) {
        deleteGroupStats(group);
        deleteGroupById(group.getId());
    }

    public void deleteGroupStats(Group group) {
        deletePaymentsByGroup(group);
        deleteRoundsByGroup(group);
    }

    //*************************
    // REPORTS
    //*************************

    public List<String> generateGroupReport(Group group) {

        /* Cache de nombres. */
        Map<Integer, String> mateNameMap = new HashMap<>();
        List<Mate> mateList = getMatesByGroup(group);
        for (Mate mate : mateList) {
            mateNameMap.put(mate.getId(), mate.getName());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<String> report = new ArrayList<>();
        report.add("REPORT FOR GROUP: " + group.getName());
        List<Round> groupList = getRoundsByGroup(group);
        for (Round round : groupList) {
            report.add("\tROUND: " + sdf.format(round.getDate()));
            List<Payment> paymentList = getPayments(round.getId());
            for (Payment payment : paymentList) {
                StringBuilder paymentTxt = new StringBuilder();
                paymentTxt.append("\t\tPAYMENT: ");
                String mateName = mateNameMap.get(payment.getIdMate());
                paymentTxt.append(mateName);
                if (payment.getNumItems() > 1) {
                    paymentTxt.append(" - " + payment.getNumItems() + " ITEMS");
                }
                if (payment.isPayer()) {
                    paymentTxt.append(" - PAYER");
                }
                report.add(paymentTxt.toString());
            }
        }
        report.add("REPORT END");
        return report;
    }

    //*************************
    // STATISTICS
    //*************************

    public Map getNumTaken(Integer idGroup) {
        Map ret = new HashMap();
        String sql = "select o.idMate, (select sum(i.numItems) from tpayment i where i.idMate = o.idMate) out " +
                "from tpayment o group by o.idMate";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ret.put(cursor.getInt(0), cursor.getInt(1));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return ret;
    }

    public Map getNumPayed(Integer idGroup) {
        Map ret = new HashMap();
        String sql = "select o.idMate, (select sum(i.numItems) from tpayment i where i.idRound in " +
                "(select ii.idRound from tpayment ii where ii.idMate = o.idMate and ii.isPayer = 1)) out " +
                "from tpayment o group by o.idMate";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ret.put(cursor.getInt(0), cursor.getInt(1));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return ret;
    }

    public void loadMateStats() {
        Map taken = getNumTaken(null);
        Map payed = getNumPayed(null);

        for (Mate mate : mMates) {
            int takenVal = taken.get(mate.getId()) != null ? (int) taken.get(mate.getId()) : 0;
            mate.getStats().setNumTaken(takenVal);
            int payedVal = payed.get(mate.getId()) != null ? (int) payed.get(mate.getId()) : 0;
            mate.getStats().setNumPayments(payedVal);
        }

        standard(mMates);
    }

    private void standard(List<Mate> mates) {

        for (Mate mate : mates) {
            int weight = mate.getStats().getNumTaken() > 0
                    ? weight = mate.getStats().getNumPayments() * 100 / mate.getStats().getNumTaken()
                    : 100;
            mate.getStats().setWeight(weight);
        }

        int max = 0;
        int min = Integer.MAX_VALUE;
        for (Mate mate : mates) {
            if (mate.isSelected()) {
                max = mate.getStats().getWeight() > max ? max = mate.getStats().getWeight() : max;
                min = mate.getStats().getWeight() < min ? min = mate.getStats().getWeight() : min;
            }
        }

        for (Mate mate : mates) {
            if (mate.isSelected()) {
                float color = (float) (mate.getStats().getWeight() - min) / (float) (max - min);
                mate.getStats().setColor(color);
            }
        }
    }

    public Group getGroup() {
        return mGroup;
    }

    public void setGroup(Group group) {
        mGroup = group;
    }
}
