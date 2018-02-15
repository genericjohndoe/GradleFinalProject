package com.udacity.gradle.builditbigger.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by joeljohnson on 2/13/18.
 */
@Entity(tableName = "tags")
public class Tag {
    @PrimaryKey
    public String name;
    public Tag(String name){
        this.name = name;
    }
}
