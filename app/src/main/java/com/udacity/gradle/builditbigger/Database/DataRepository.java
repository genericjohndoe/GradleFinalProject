package com.udacity.gradle.builditbigger.Database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.udacity.gradle.builditbigger.Models.HilarityUser;

/**
 * Created by joeljohnson on 2/8/18.
 */

public class DataRepository {
    private HilarityUserDatabase hilarityUserDatabase;
    private HilarityUserLiveData hilarityUserLiveData;
    private static DataRepository dataRepository;

    private DataRepository(Context context){
        hilarityUserDatabase = HilarityUserDatabase.getInstance(context);
        hilarityUserLiveData = new HilarityUserLiveData(hilarityUserDatabase);
    }

    public static DataRepository getInstance(Context context){
        if (dataRepository == null) dataRepository = new DataRepository(context);
        return dataRepository;
    }

    public LiveData<HilarityUser> getUsers(String name){
        return hilarityUserDatabase.dao().getUsersFromName(name);
    }

    public HilarityUserLiveData getHilarityUserLiveData() {
        return hilarityUserLiveData;
    }
}
