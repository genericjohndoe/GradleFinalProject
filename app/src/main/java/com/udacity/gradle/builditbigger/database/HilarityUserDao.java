package com.udacity.gradle.builditbigger.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.udacity.gradle.builditbigger.models.HilarityUser;

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
    /*@Query("SELECT * FROM hilarityusers WHERE uid in :uidList")
    HilarityUser[] getUsersFromUidList(String[] uidList);*/
    @Query("SELECT userName FROM hilarityusers WHERE uid = :uid")
    String getUserName(String uid);
}
