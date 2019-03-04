package me.larrycarodenis.domain;

import java.util.List;

public class GenderTotals {

    private int M;
    private int F;
    private List<Classification> classificationList;

    public GenderTotals() {

    }

    public void setM(int M) {
        this.M = M;
    }

    public void setF(int F) {
        this.F = F;
    }

    public int getM() {
        return M;
    }

    public int getF() {
        return F;
    }

    public void setClassificationList(List<Classification> classificationList) {
        this.classificationList = classificationList;
    }

    public List<Classification> getClassificationList()
    {
        return classificationList;
    }







}
