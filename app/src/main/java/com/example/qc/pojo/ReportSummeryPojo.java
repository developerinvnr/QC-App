package com.example.qc.pojo;

public class ReportSummeryPojo {
    int srno;
    String crop;
    String variety;
    String noofLots;

    public ReportSummeryPojo(int srno, String crop, String variety, String noofLots) {
        this.srno = srno;
        this.crop = crop;
        this.variety = variety;
        this.noofLots = noofLots;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getNoofLots() {
        return noofLots;
    }

    public void setNoofLots(String noofLots) {
        this.noofLots = noofLots;
    }
}
