package es.humarbean.gespagos.models;

public class MateStats {
    private int mNumRows;
    private int mNumPayments;
    private int mNumTaken;
    private int mNumBorrowed;
    private int mWeight = 100;

    public int getNumRows() {
        return mNumRows;
    }

    public void setNumRows(int numRows) {
        mNumRows = numRows;
    }

    public int getNumPayments() {
        return mNumPayments;
    }

    public void setNumPayments(int numPayments) {
        mNumPayments = numPayments;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public int getNumTaken() {
        return mNumTaken;
    }

    public void setNumTaken(int numTaken) {
        mNumTaken = numTaken;
    }

    public int getNumBorrowed() {
        return mNumBorrowed;
    }

    public void setNumBorrowed(int numBorrowed) {
        mNumBorrowed = numBorrowed;
    }
}
