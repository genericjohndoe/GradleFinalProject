package com.udacity.gradle.builditbigger.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


/**
 * Created by joeljohnson on 2/6/18.
 */
@Database(entities = {UserName.class}, version = 1)
public abstract class UserNameDatabase extends RoomDatabase {

    private static UserNameDatabase sInstance;
    public abstract UserNameDAO dao();

    public static synchronized UserNameDatabase  getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), UserNameDatabase.class, "usernames")
                    .build();
        }
        return sInstance;
    }
}
