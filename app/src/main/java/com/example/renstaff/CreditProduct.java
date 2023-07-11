package com.example.renstaff;

public class CreditProduct {
    private String name;
    private double interestRate;
    private int minTerm, maxTerm;

    public CreditProduct(String name, double interestRate, int minTerm, int maxTerm){
        this.name = name;
        if (interestRate < 0) {
            throw new IllegalArgumentException("Error! Interest rate can't be less than 0!");
        }
        this.interestRate = interestRate;
        this.minTerm = minTerm;
        this.maxTerm = maxTerm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getMaxTerm() {
        return maxTerm;
    }

    public void setMaxTerm(int maxTerm) {
        this.maxTerm = maxTerm;
    }

    public int getMinTerm() {
        return minTerm;
    }

    public void setMinTerm(int minTerm) {
        this.minTerm = minTerm;
    }
}
