package com.udacity.gradle.builditbigger.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by joeljohnson on 2/13/18.
 */

public abstract class TagDatabase extends RoomDatabase {
    private static TagDatabase sInstance;
    public abstract TagDAO dao();

    public static synchronized TagDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), TagDatabase.class, "tags")
                    .build();
        }
        return sInstance;
    }
}
