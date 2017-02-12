package es.humarbean.gespagos.models;

public class Payment {
    private Integer mId;
    private Integer mIdRound;
    private Integer mIdMate;
    private Integer mNumItems;
    private Boolean mPayer;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Integer getIdRound() {
        return mIdRound;
    }

    public void setIdRound(Integer idRound) {
        mIdRound = idRound;
    }

    public Integer getIdMate() {
        return mIdMate;
    }

    public void setIdMate(Integer idMate) {
        mIdMate = idMate;
    }

    public Integer getNumItems() {
        return mNumItems;
    }

    public void setNumItems(Integer numItems) {
        mNumItems = numItems;
    }

    public Boolean isPayer() {
        return mPayer;
    }

    public void setPayer(Boolean payer) {
        mPayer = payer;
    }

}
