package com.mustapha.fishingsystemgs.classes;

public class TS {
    private String name; //name formed by appending third decimal of PGR to the end
    //TS name
    private double thirdDecimalOfMother;
    private String dnaOfMother;

    private String id;
    private String tsName; //name of the TS
    public TS() {
    }

    public TS(String name, double thirdDecimalOfMother, String dnaOfMother, String id, String tsName) {
        this.name = name;
        this.thirdDecimalOfMother = thirdDecimalOfMother;
        this.dnaOfMother = dnaOfMother;
        this.id = id;
        this.tsName = tsName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }
}
