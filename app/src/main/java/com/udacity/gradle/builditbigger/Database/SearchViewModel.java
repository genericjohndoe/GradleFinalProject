package com.udacity.gradle.builditbigger.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.gradle.builditbigger.Models.HilarityUser;

import java.util.List;

/**
 * Created by joeljohnson on 2/8/18.
 */

public class SearchViewModel extends AndroidViewModel {
    DataRepository dataRepository;
    TempUserLiveData tempUserLiveData;

    public SearchViewModel(Application application){
        super(application);
        //dataRepository = DataRepository.getInstance(application.getApplicationContext());
        tempUserLiveData = new TempUserLiveData();
    }

    /*public LiveData<HilarityUser> getUsersFromName(String name){
        return dataRepository.getUsers(name);
    }

    public LiveData<String> getTags(String tag){
        return dataRepository.getTags(tag);
    }*/

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public TempUserLiveData getTempUserLiveData() {
        return tempUserLiveData;
    }

    /*public LiveData<String> getUserName(String uid){
        return dataRepository.getUsername(uid);
    }

    public LiveData<List<String>> getUidList(String userName){
        return dataRepository.getUidList(userName);
    }

    public String getUid(String userName){
        return dataRepository.getUID(userName);
    }*/
}
