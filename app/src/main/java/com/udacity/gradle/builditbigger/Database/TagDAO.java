package com.udacity.gradle.builditbigger.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by joeljohnson on 2/13/18.
 */
@Dao
public interface TagDAO {
    @Insert void insert(Tag name);
    @Delete void delete(Tag name);
    @Query("SELECT name FROM tags WHERE name LIKE :tagName")
    LiveData<String> searchForTags(String tagName);
}
