package com.example.qc.parser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("gotrepdetailsarray")
    @Expose
    private List<Gotrepdetailsarray> gotrepdetailsarray = null;

    public List<Gotrepdetailsarray> getGotrepdetailsarray() {
        return gotrepdetailsarray;
    }

    public void setGotrepdetailsarray(List<Gotrepdetailsarray> gotrepdetailsarray) {
        this.gotrepdetailsarray = gotrepdetailsarray;
    }
}
