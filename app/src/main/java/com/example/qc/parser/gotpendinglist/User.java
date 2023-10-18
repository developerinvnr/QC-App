package com.example.qc.parser.gotpendinglist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("samparray")
    @Expose
    private List<Samparray> samparray;

    public List<Samparray> getSamparray() {
        return samparray;
    }

    public void setSamparray(List<Samparray> samparray) {
        this.samparray = samparray;
    }

}
