package me.larrycarodenis.domain;

import java.time.LocalTime;

public class Metrics
{
    private int numberOfStores;
    private double femaleRatio;
    private LocalTime lastClassification;
    private double ageMedian;

    public Metrics()
    {

    }

    public int getNumberOfStores() {
        return numberOfStores;
    }

    public void setNumberOfStores(int numberOfStores) {
        this.numberOfStores = numberOfStores;
    }

    public double getFemaleRatio() {
        return femaleRatio;
    }

    public void setFemaleRatio(double maleFemaleRatio) {
        this.femaleRatio = maleFemaleRatio;
    }

    public LocalTime getLastClassification() {
        return lastClassification;
    }

    public void setLastClassification(LocalTime lastClassification) {
        this.lastClassification = lastClassification;
    }

    public double getAgeMedian() {
        return ageMedian;
    }

    public void setAgeMedian(double ageMedian) {
        this.ageMedian = ageMedian;
    }

    public String toString()
    {
        return "AgeMedian: " + getAgeMedian() + "Female ratio: " + getFemaleRatio() + "LastClassification: " + getLastClassification() + "Number of stores: " + getNumberOfStores();
    }





}
