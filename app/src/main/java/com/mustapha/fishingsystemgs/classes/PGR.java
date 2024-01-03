package com.mustapha.fishingsystemgs.classes;

public class PGR {
    private String name;
    private double firstDecimalNumber;
    private double secondDecimalNumber;
    private double thirdDecimalNumber;
    private String dna;
    public PGR() {
    }

    public PGR(String name, double firstDecimalNumber,
               double secondDecimalNumber, double thirdDecimalNumber, String dna) {
        this.name = name;
        this.firstDecimalNumber = firstDecimalNumber;
        this.secondDecimalNumber = secondDecimalNumber;
        this.thirdDecimalNumber = thirdDecimalNumber;
        this.dna = dna;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFirstDecimalNumber() {
        return firstDecimalNumber;
    }

    public void setFirstDecimalNumber(double firstDecimalNumber) {
        this.firstDecimalNumber = firstDecimalNumber;
    }

    public double getSecondDecimalNumber() {
        return secondDecimalNumber;
    }

    public void setSecondDecimalNumber(double secondDecimalNumber) {
        this.secondDecimalNumber = secondDecimalNumber;
    }

    public double getThirdDecimalNumber() {
        return thirdDecimalNumber;
    }

    public void setThirdDecimalNumber(double thirdDecimalNumber) {
        this.thirdDecimalNumber = thirdDecimalNumber;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }
}
