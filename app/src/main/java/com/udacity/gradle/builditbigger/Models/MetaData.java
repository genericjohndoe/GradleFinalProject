package com.udacity.gradle.builditbigger.Models;

import java.util.List;

/**
 * Created by joeljohnson on 3/4/18.
 */

public class MetaData {
    private String type;
    private int number;
    private List<String> tags;

    public MetaData(String type, int number, List<String> tags){
        this.type = type;
        this.number = number;
        this.tags = tags;
    }

    public int getNumber() {
        return number;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }
}
