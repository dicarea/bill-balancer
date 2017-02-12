package es.humarbean.gespagos.models;

public class Mate {
    private Integer mId;
    private String mName;
    private boolean mSelected = false;
    private boolean mPayer = false;
    private int mNumPayments = 0;
    private int mNumRounds = 0;
    private int mNumMatesInPayments = 0;
    private int mNumMatesInRounds = 0;
    private int mMultiplier = 2;
    private boolean mMultiplierActive = false;

    public void resetValues() {
        setNumPayments(0);
        setNumRounds(0);
        setNumMatesInPayments(0);
        setNumMatesInRounds(0);
        setMultiplier(2);
        setmMultiplierActive(false);
    }

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

    public int getMultiplier() {
        return mMultiplier;
    }

    public void setMultiplier(int multiplier) {
        mMultiplier = multiplier;
    }

    public boolean ismMultiplierActive() {
        return mMultiplierActive;
    }

    public void setmMultiplierActive(boolean mMultiplierActive) {
        this.mMultiplierActive = mMultiplierActive;
    }

}
