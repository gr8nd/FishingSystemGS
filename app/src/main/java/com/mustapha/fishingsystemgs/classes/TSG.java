package com.mustapha.fishingsystemgs.classes;

public class TSG {
    private String name;
    private String dna;
    public TSG() {
    }

    public TSG(String name, String dna) {
        this.name = name;
        this.dna = dna;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDna() {
        return dna;
    }

    public void setDna(String dna) {
        this.dna = dna;
    }
}
