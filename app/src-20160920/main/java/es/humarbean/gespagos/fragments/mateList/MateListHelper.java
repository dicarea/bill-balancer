package es.humarbean.gespagos.fragments.mateList;

import android.util.Log;

import java.util.Date;
import java.util.List;

import es.humarbean.gespagos.database.GesPagosDataSource;
import es.humarbean.gespagos.models.Group;
import es.humarbean.gespagos.models.Mate;
import es.humarbean.gespagos.models.Payment;
import es.humarbean.gespagos.models.Round;

public class MateListHelper {

    private CalcMode mCalcMode = CalcMode.PRO;
    private GesPagosDataSource ds;

    public MateListHelper() {
        ds = GesPagosDataSource.getInstance();
    }

    public void resetGroup(Group group) {
        ds.deleteGroupStats(group);
    }

    public int getNumMates() {
        return ds.getListSize();
    }

    public Mate getMateInPosition(int pos) {
        return ds.getMateInPosition(pos);
    }

    private void resetAllPayersFlag() {
        ds.resetAllPayersFlag();
    }

    public int getMatePositionById(Integer id) {
        return ds.getMatePositionById(id);
    }

    public void markMultiplier(Integer mateId, boolean checked) {
        ds.getMateById(mateId).setMultiplierActive(checked);
    }

    public void markMateSelection(Integer mateId, boolean checked) {
        Mate mate = ds.getMateById(mateId);
        mate.setSelected(checked);

        ds.persistMate(mate);
    }

    public void markMateAsPayer(Integer mateId) {
        this.resetAllPayersFlag();
        ds.getMateById(mateId).setPayer(true);
    }

    public void incrementMultiplier(Integer mateId) {
        Mate mate = ds.getMateById(mateId);
        mate.setMultiplier(mate.getMultiplier() + 1);
    }

    public boolean isInCalcMode(CalcMode valToCheck) {
        return mCalcMode.equals(valToCheck);
    }

    public void changeCalculationMode() {
        switch (mCalcMode) {
            case PRO:
                mCalcMode = CalcMode.SIMPLE;
                break;
            case SIMPLE:
                mCalcMode = CalcMode.PRO;
                break;
            default:
                mCalcMode = CalcMode.PRO;
        }
    }

    public void insertRoundAndPayments(Integer idGroup) {

        List<Mate> mates = ds.getMates();

        Round round = new Round();
        round.setIdGroup(idGroup);
        round.setDate(new Date());
        ds.addRound(round);

        for (Mate mate : mates) {
            if (mate.isSelected()) {
                Payment payment = new Payment();
                payment.setIdGroup(idGroup);
                payment.setIdRound(round.getId());
                payment.setIdMate(mate.getId());
                payment.setNumItems(mate.isMultiplierActive() ? mate.getMultiplier() : 1);
                payment.setPayer(mate.isPayer());
                ds.addPayment(payment);
            }
        }

        /* Se actualizan las estadisticas con cada pago registrado. */
        ds.loadMateStats();
    }

    public List<Round> getAllRoundsAndPayments() {
        List<Round> rounds = ds.getRounds();

        for (Round round : rounds) {
            List<Payment> payments = ds.getPayments(round.getId());
            round.setPayments(payments);
        }

        return rounds;
    }

    public List<Group> getGroups() {
        return ds.getGroups();
    }

    public void loadMates(Group group) {
        ds.loadMates(group);
    }

    public void loadMateStats() {
        ds.loadMateStats();
    }

    public void printRounds() {
        List<Round> rounds = getAllRoundsAndPayments();
        for (Round round : rounds) {
            Log.d("ROUND", round.getId() + " - " + round.getDate());
            for (Payment payment : round.getPayments()) {
                Log.d("PAYMENT", payment.getId() + " - " + payment.getIdRound() + " - "
                        + payment.getIdMate() + " - " + payment.getNumItems() + " - "
                        + payment.isPayer());
            }
        }
    }

    public Mate getMateById(Integer id) {
        return ds.getMateById(id);
    }

    public Group getGroupById(Integer id) {
        return ds.getGroupById(id);
    }

    public void deleteGroup(Group group) {
        ds.deleteGroup(group);
    }

    public Group getGroup() {
        return ds.getGroup();
    }

    public void setGroup(Group group) {
        ds.setGroup(group);
    }

    public static enum CalcMode {
        SIMPLE,
        PRO;
    }

    public void selectGroup(Group group) {
        ds.setSelectedGroup(group);
    }

    public Group getSelectedGroup() {
        return ds.getSelectedGroup();
    }
}
