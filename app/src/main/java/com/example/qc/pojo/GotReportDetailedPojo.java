package com.example.qc.pojo;

public class GotReportDetailedPojo {

    int srno;
    String arrivaldate;
    String crop;
    String spcodes;
    String variety;
    String lotno;
    String sampleno;
    String nob;
    String qty;
    String gotstatus;
    String prodloc;
    String organizer;
    String farmer;
    String prodper;
    String sloc;
    String dateofsowing;
    String sownurseryloc;
    String sowbedno;
    String sowtrayno;
    String sownooftray;
    String tpsowloc;
    String dateoftp;
    String tpbedno;
    String tpdirection;
    String ttivresultdate;

    public GotReportDetailedPojo(int srno, String arrivaldate, String crop, String spcodes, String variety, String lotno, String sampleno, String nob, String qty, String gotstatus, String prodloc, String organizer, String farmer, String prodper, String sloc) {
        this.srno = srno;
        this.arrivaldate = arrivaldate;
        this.crop = crop;
        this.spcodes = spcodes;
        this.variety = variety;
        this.lotno = lotno;
        this.sampleno = sampleno;
        this.nob = nob;
        this.qty = qty;
        this.gotstatus = gotstatus;
        this.prodloc = prodloc;
        this.organizer = organizer;
        this.farmer = farmer;
        this.prodper = prodper;
        this.sloc = sloc;
    }

    public GotReportDetailedPojo(int srno, String arrivaldate, String crop, String spcodes, String variety, String lotno, String sampleno, String dateofsowing, String sownurseryloc, String sowtrayno, String sownooftray, String tpbedno) {
        this.srno = srno;
        this.arrivaldate = arrivaldate;
        this.crop = crop;
        this.spcodes = spcodes;
        this.variety = variety;
        this.lotno = lotno;
        this.sampleno = sampleno;
        this.dateofsowing = dateofsowing;
        this.sownurseryloc = sownurseryloc;
        this.sowtrayno = sowtrayno;
        this.sownooftray = sownooftray;
        this.tpbedno = tpbedno;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public String getArrivaldate() {
        return arrivaldate;
    }

    public void setArrivaldate(String arrivaldate) {
        this.arrivaldate = arrivaldate;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getSpcodes() {
        return spcodes;
    }

    public void setSpcodes(String spcodes) {
        this.spcodes = spcodes;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getSampleno() {
        return sampleno;
    }

    public void setSampleno(String sampleno) {
        this.sampleno = sampleno;
    }

    public String getNob() {
        return nob;
    }

    public void setNob(String nob) {
        this.nob = nob;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getGotstatus() {
        return gotstatus;
    }

    public void setGotstatus(String gotstatus) {
        this.gotstatus = gotstatus;
    }

    public String getProdloc() {
        return prodloc;
    }

    public void setProdloc(String prodloc) {
        this.prodloc = prodloc;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getFarmer() {
        return farmer;
    }

    public void setFarmer(String farmer) {
        this.farmer = farmer;
    }

    public String getProdper() {
        return prodper;
    }

    public void setProdper(String prodper) {
        this.prodper = prodper;
    }

    public String getSloc() {
        return sloc;
    }

    public void setSloc(String sloc) {
        this.sloc = sloc;
    }

    public String getDateofsowing() {
        return dateofsowing;
    }

    public void setDateofsowing(String dateofsowing) {
        this.dateofsowing = dateofsowing;
    }

    public String getSownurseryloc() {
        return sownurseryloc;
    }

    public void setSownurseryloc(String sownurseryloc) {
        this.sownurseryloc = sownurseryloc;
    }

    public String getSowbedno() {
        return sowbedno;
    }

    public void setSowbedno(String sowbedno) {
        this.sowbedno = sowbedno;
    }

    public String getSowtrayno() {
        return sowtrayno;
    }

    public void setSowtrayno(String sowtrayno) {
        this.sowtrayno = sowtrayno;
    }

    public String getSownooftray() {
        return sownooftray;
    }

    public void setSownooftray(String sownooftray) {
        this.sownooftray = sownooftray;
    }

    public String getTpsowloc() {
        return tpsowloc;
    }

    public void setTpsowloc(String tpsowloc) {
        this.tpsowloc = tpsowloc;
    }

    public String getDateoftp() {
        return dateoftp;
    }

    public void setDateoftp(String dateoftp) {
        this.dateoftp = dateoftp;
    }

    public String getTpbedno() {
        return tpbedno;
    }

    public void setTpbedno(String tpbedno) {
        this.tpbedno = tpbedno;
    }

    public String getTpdirection() {
        return tpdirection;
    }

    public void setTpdirection(String tpdirection) {
        this.tpdirection = tpdirection;
    }

    public String getTtivresultdate() {
        return ttivresultdate;
    }

    public void setTtivresultdate(String ttivresultdate) {
        this.ttivresultdate = ttivresultdate;
    }
}
