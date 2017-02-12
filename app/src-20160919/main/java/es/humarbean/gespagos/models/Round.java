package es.humarbean.gespagos.models;

import java.util.Date;
import java.util.List;

public class Round {
    private Integer mId;
    private Integer mIdGroup;
    private Date mDate;
    private String mPlace;

    private List<Payment> payments;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Integer getIdGroup() {
        return mIdGroup;
    }

    public void setIdGroup(Integer idGroup) {
        mIdGroup = idGroup;
    }
}
