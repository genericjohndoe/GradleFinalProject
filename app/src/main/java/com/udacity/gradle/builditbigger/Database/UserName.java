package com.udacity.gradle.builditbigger.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by joeljohnson on 2/6/18.
 */
@Entity(tableName = "usernames")
public class UserName {
    @PrimaryKey @NonNull
    public String uid;
    @NonNull
    public String userName;

    public UserName(@NonNull String key,@NonNull String value){
        uid = key;
        userName = value;
    }
}
