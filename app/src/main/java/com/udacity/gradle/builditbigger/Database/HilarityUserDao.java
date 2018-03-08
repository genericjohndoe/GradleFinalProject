package com.udacity.gradle.builditbigger.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.gradle.builditbigger.Models.HilarityUser;

import java.util.List;

/**
 * Created by joeljohnson on 2/13/18.
 */
@Dao
public interface HilarityUserDao {
    @Insert
    void insert(HilarityUser hu);
    @Update
    void update(HilarityUser hu);
    @Delete
    void delete(HilarityUser hu);
    @Query("SELECT * FROM hilarityusers WHERE userName LIKE :userName")
    LiveData<HilarityUser> getUsersFromName(String userName);
    @Query("SELECT * FROM hilarityusers WHERE uid in :uidList")
    HilarityUser[] getUsersFromUidList(String[] uid);
    @Query("SELECT userName FROM hilarityusers WHERE uid = :uid")
    String getUserName(String uid);
}
