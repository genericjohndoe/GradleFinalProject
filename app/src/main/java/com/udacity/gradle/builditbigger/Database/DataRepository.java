package com.udacity.gradle.builditbigger.Database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.udacity.gradle.builditbigger.Models.HilarityUser;

/**
 * Created by joeljohnson on 2/8/18.
 */

public class DataRepository {
    //private UserNameDatabase userNameDatabase;
    //private UserNamesLiveData userNamesLiveData;
    private HilarityUserDatabase hilarityUserDatabase;
    private HilarityUserLiveData hilarityUserLiveData;
    private TagDatabase tagDatabase;
    private TagLiveData tagLiveData;
    private static DataRepository dataRepository;

    private DataRepository(Context context){
        //userNameDatabase = UserNameDatabase.getInstance(context);
        //userNamesLiveData = new UserNamesLiveData(userNameDatabase);
        hilarityUserDatabase = HilarityUserDatabase.getInstance(context);
        hilarityUserLiveData = new HilarityUserLiveData(hilarityUserDatabase);
        tagDatabase = TagDatabase.getInstance(context);
        tagLiveData = new TagLiveData(tagDatabase);
    }

    public static DataRepository getInstance(Context context){
        if (dataRepository == null){
            dataRepository = new DataRepository(context);
        }
        return dataRepository;
    }

    public LiveData<HilarityUser> getUsers(String name){
        return hilarityUserDatabase.dao().getUsersFromName(name);
    }

    public LiveData<String> getTags(String tag){
        return tagDatabase.dao().searchForTags(tag);
    }

    public HilarityUserLiveData getHilarityUserLiveData() {
        return hilarityUserLiveData;
    }

    /*public String getUID(String userName){
        return userNameDatabase.dao().getSpecificUID(userName);
    }

    public LiveData<String> getUsername(String uid){
        return userNameDatabase.dao().getSpecificUserName(uid);
    }
    public LiveData<List<String>> getUidList(String userName){
        return userNameDatabase.dao().getUidList(userName);
    }*/
}
