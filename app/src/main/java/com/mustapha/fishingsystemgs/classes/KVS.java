package com.mustapha.fishingsystemgs.classes;

public class KVS {

    private String name; //name formed by appending third decimal of PGR to the end
    //TS name
    private String dnaOfMother;

    private String id;


    public KVS() {
    }

    public KVS(String name, String dnaOfMother, String id) {
        this.name = name;
        this.dnaOfMother = dnaOfMother;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
