package com.example.rana.mytrippmate.pojoclasses;

/**
 * Created by Rana on 12/21/2018.
 */

public class EventExpenditure {

    private String expId;
    private String expComment;
    private double expCost;

    public EventExpenditure() {
    }

    public EventExpenditure(String expId, String expComment, double expCost) {
        this.expId = expId;
        this.expComment = expComment;
        this.expCost = expCost;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public String getExpComment() {
        return expComment;
    }

    public void setExpComment(String expComment) {
        this.expComment = expComment;
    }

    public double getExpCost() {
        return expCost;
    }

    public void setExpCost(double expCost) {
        this.expCost = expCost;
    }
}
