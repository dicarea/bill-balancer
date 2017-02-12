package es.humarbean.gespagos.models;

public class Mate {
    private Integer mId;
    private String mName;
    private boolean mSelected;
    private boolean mPayer;
    private long mLastPayerSelect;
    private int mNumPayments;
    private int mNumRounds;
    private int mNumMatesInPayments;
    private int mNumMatesInRounds;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public int getNumPayments() {
        return mNumPayments;
    }

    public void setNumPayments(int numPayments) {
        mNumPayments = numPayments;
    }

    public int getNumRounds() {
        return mNumRounds;
    }

    public void setNumRounds(int numRounds) {
        mNumRounds = numRounds;
    }

    public long getLastPayerSelect() {
        return mLastPayerSelect;
    }

    public void setLastPayerSelect(long lastPayerSelect) {
        mLastPayerSelect = lastPayerSelect;
    }

    public boolean isPayer() {
        return mPayer;
    }

    public void setPayer(boolean payer) {
        mPayer = payer;
    }

    public int getNumMatesInPayments() {
        return mNumMatesInPayments;
    }

    public void setNumMatesInPayments(int numMatesInPayments) {
        mNumMatesInPayments = numMatesInPayments;
    }

    public int getNumMatesInRounds() {
        return mNumMatesInRounds;
    }

    public void setNumMatesInRounds(int numMatesInRounds) {
        mNumMatesInRounds = numMatesInRounds;
    }
}
