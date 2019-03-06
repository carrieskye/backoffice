package me.larrycarodenis.domain;

import java.time.LocalDateTime;


public class Metrics {
    private int numberOfDevices;
    private int numberOfClassifications;
    private double femaleRatio;
    private int averageAge;
    private int distinctCustomers;
    private Classification lastClassification;


    public int getNumberOfDevices() {
        return numberOfDevices;
    }

    public void setNumberOfDevices(int numberOfDevices) {
        this.numberOfDevices = numberOfDevices;
    }

    public int getNumberOfClassifications() {
        return numberOfClassifications;
    }

    public void setNumberOfClassifications(int numberOfClassifications) {
        this.numberOfClassifications = numberOfClassifications;
    }

    public double getFemaleRatio() {
        return femaleRatio;
    }

    public void setFemaleRatio(double femaleRatio) {
        this.femaleRatio = femaleRatio;
    }

    public int getAverageAge() {
        return averageAge;
    }

    public void setAverageAge(int averageAge) {
        this.averageAge = averageAge;
    }

    public int getDistinctCustomers() {
        return distinctCustomers;
    }

    public void setDistinctCustomers(int distinctCustomers) {
        this.distinctCustomers = distinctCustomers;
    }

    public Classification getLastClassification() {
        return lastClassification;
    }

    public void setLastClassification(Classification lastClassification) {
        this.lastClassification = lastClassification;
    }

}
