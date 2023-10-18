package com.example.qc.parser.gotpendinglist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Samparray {
    @SerializedName("sampleno")
    @Expose
    private String sampleno;
    @SerializedName("fnobser_noofplants")
    @Expose
    private Integer fnobserNoofplants;
    @SerializedName("fnobser_maleplants")
    @Expose
    private Integer fnobserMaleplants;
    @SerializedName("fnobser_femaleplants")
    @Expose
    private Integer fnobserFemaleplants;
    @SerializedName("fnobser_otherofftype")
    @Expose
    private Integer fnobserOtherofftype;
    @SerializedName("fnobser_obserdate")
    @Expose
    private String fnobserObserdate;
    @SerializedName("fnobser_remarks")
    @Expose
    private String fnobserRemarks;
    @SerializedName("gppercentage")
    @Expose
    private Double gppercentage;

    public String getSampleno() {
        return sampleno;
    }

    public void setSampleno(String sampleno) {
        this.sampleno = sampleno;
    }

    public Integer getFnobserNoofplants() {
        return fnobserNoofplants;
    }

    public void setFnobserNoofplants(Integer fnobserNoofplants) {
        this.fnobserNoofplants = fnobserNoofplants;
    }

    public Integer getFnobserMaleplants() {
        return fnobserMaleplants;
    }

    public void setFnobserMaleplants(Integer fnobserMaleplants) {
        this.fnobserMaleplants = fnobserMaleplants;
    }

    public Integer getFnobserFemaleplants() {
        return fnobserFemaleplants;
    }

    public void setFnobserFemaleplants(Integer fnobserFemaleplants) {
        this.fnobserFemaleplants = fnobserFemaleplants;
    }

    public Integer getFnobserOtherofftype() {
        return fnobserOtherofftype;
    }

    public void setFnobserOtherofftype(Integer fnobserOtherofftype) {
        this.fnobserOtherofftype = fnobserOtherofftype;
    }

    public String getFnobserObserdate() {
        return fnobserObserdate;
    }

    public void setFnobserObserdate(String fnobserObserdate) {
        this.fnobserObserdate = fnobserObserdate;
    }

    public String getFnobserRemarks() {
        return fnobserRemarks;
    }

    public void setFnobserRemarks(String fnobserRemarks) {
        this.fnobserRemarks = fnobserRemarks;
    }

    public Double getGppercentage() {
        return gppercentage;
    }

    public void setGppercentage(Double gppercentage) {
        this.gppercentage = gppercentage;
    }
}
