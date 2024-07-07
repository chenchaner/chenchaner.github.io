package com.example.dictionary;

import org.json.JSONArray;

public class Translation {
    private String part;
    private String mean;
    public Translation(String part, String mean){
        this.mean = mean;
        this.part = part;
    }

    public String getMean() {
        return mean;
    }

    public String getPart() {
        return part;
    }
}
