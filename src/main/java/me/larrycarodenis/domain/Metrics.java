package me.larrycarodenis.domain;

import java.time.LocalTime;


public class Metrics
{
    private int numberOfStores;
    private double femaleRatio;
    private LocalTime lastClassification;
    private double ageMedian;
    private int distinctCustomers;

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

    public int getDistinctCustomers()
    {
        return distinctCustomers;
    }

    public void setDistinctCustomers(int distinctCustomers)
    {
        this.distinctCustomers = distinctCustomers;
    }

    public String toString()
    {
        return "AgeMedian: " + getAgeMedian() + "Female ratio: " + getFemaleRatio() + "LastClassification: " + getLastClassification() + "Number of stores: " + getNumberOfStores();
    }





}
