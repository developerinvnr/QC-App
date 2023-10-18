package com.example.qc.adapter;

public class FMV_Data {
    int i;
    String fmv_crophealth;
    String fmv_reasons;
    String fmv_noofplants;

    public FMV_Data() {
    }

    public FMV_Data(int i, String fmv_crophealth, String fmv_reasons, String fmv_noofplants) {
        this.i = i;
        this.fmv_crophealth = fmv_crophealth;
        this.fmv_reasons = fmv_reasons;
        this.fmv_noofplants = fmv_noofplants;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public String getFmv_crophealth() {
        return fmv_crophealth;
    }

    public void setFmv_crophealth(String fmv_crophealth) {
        this.fmv_crophealth = fmv_crophealth;
    }

    public String getFmv_reasons() {
        return fmv_reasons;
    }

    public void setFmv_reasons(String fmv_reasons) {
        this.fmv_reasons = fmv_reasons;
    }

    public String getFmv_noofplants() {
        return fmv_noofplants;
    }

    public void setFmv_noofplants(String fmv_noofplants) {
        this.fmv_noofplants = fmv_noofplants;
    }
}
