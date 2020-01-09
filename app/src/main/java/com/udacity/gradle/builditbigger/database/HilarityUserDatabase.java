package com.udacity.gradle.builditbigger.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.udacity.gradle.builditbigger.models.HilarityUser;

/**
 * Created by joeljohnson on 2/13/18.
 */
@Database(entities = {HilarityUser.class}, version = 1, exportSchema = false)
public abstract class HilarityUserDatabase extends RoomDatabase {
    private static HilarityUserDatabase sInstance;
    public abstract HilarityUserDao dao();

    public static synchronized HilarityUserDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), HilarityUserDatabase.class, "users")
                    .build();
        }
        return sInstance;
    }
}
