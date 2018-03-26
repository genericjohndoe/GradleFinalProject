package com.udacity.gradle.builditbigger.Models;

import java.util.Map;

/**
 * MetaData class serves as model for post meta data
 */

public class MetaData {
    private String type;
    private int number;
    private Map<String, Boolean> tags;

    public MetaData(){}

    public MetaData(String type, int number, Map<String, Boolean> tags){
        this.type = type;
        this.number = number;
        this.tags = tags;
    }

    public int getNumber() {
        return number;
    }

    public Map<String, Boolean> getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }
}
