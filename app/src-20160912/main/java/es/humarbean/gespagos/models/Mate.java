package es.humarbean.gespagos.models;

public class Mate {
    private Integer mId;
    private Integer idGroup;
    private String mName;
    private boolean mSelected = false;
    private boolean mPayer = false;
    private int mMultiplier = 2;
    private boolean mMultiplierActive = false;
    private MateStats mStats = new MateStats();

    public void resetValues() {
        setMultiplier(2);
        setMultiplierActive(false);
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

    public boolean isPayer() {
        return mPayer;
    }

    public void setPayer(boolean payer) {
        mPayer = payer;
    }

    public int getMultiplier() {
        return mMultiplier;
    }

    public void setMultiplier(int multiplier) {
        mMultiplier = multiplier;
    }

    public MateStats getStats() {
        return mStats;
    }

    public void setStats(MateStats stats) {
        mStats = stats;
    }

    public boolean isMultiplierActive() {
        return mMultiplierActive;
    }

    public void setMultiplierActive(boolean multiplierActive) {
        mMultiplierActive = multiplierActive;
    }

    public Integer getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(Integer idGroup) {
        this.idGroup = idGroup;
    }
}
