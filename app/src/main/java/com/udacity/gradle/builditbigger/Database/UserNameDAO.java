package com.udacity.gradle.builditbigger.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by joeljohnson on 2/6/18.
 */
@Dao
public interface UserNameDAO {
    @Insert
    void insertUserName(UserName userName);
    @Update
    void updateUserName(UserName userName);
    @Delete
    void deleteUserName(UserName userName);
    @Query("SELECT uid FROM usernames WHERE userName = :userName")
    String getSpecificUID(String userName);
    @Query("SELECT userName FROM usernames WHERE uid = :uid")
    LiveData<String> getSpecificUserName(String uid);
    @Query("SELECT uid FROM usernames WHERE userName LIKE :userName")
    LiveData<List<String>> getUidList(String userName);
    //todo make sure to add % % at beginning and ending of user name in search
}
