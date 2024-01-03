package com.mustapha.fishingsystemgs.classes;

public class TS {
    private String name;
    private double thirdDecimalOfMother;
    private String dnaOfMother;
    public TS() {
    }

    public TS(String name, double thirdDecimalOfMother, String dnaOfMother) {
        this.name = name;
        this.thirdDecimalOfMother = thirdDecimalOfMother;
        this.dnaOfMother = dnaOfMother;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getThirdDecimalOfMother() {
        return thirdDecimalOfMother;
    }

    public void setThirdDecimalOfMother(double thirdDecimalOfMother) {
        this.thirdDecimalOfMother = thirdDecimalOfMother;
    }

    public String getDnaOfMother() {
        return dnaOfMother;
    }

    public void setDnaOfMother(String dnaOfMother) {
        this.dnaOfMother = dnaOfMother;
    }
}
