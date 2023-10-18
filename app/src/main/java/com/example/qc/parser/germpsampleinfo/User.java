package com.example.qc.parser.germpsampleinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("samplegemparray")
    @Expose
    private Samplegemparray samplegemparray;

    public Samplegemparray getSamplegemparray() {
        return samplegemparray;
    }

    public void setSamplegemparray(Samplegemparray samplegemparray) {
        this.samplegemparray = samplegemparray;
    }
}
