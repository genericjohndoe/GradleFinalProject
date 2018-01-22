package com.udacity.gradle.builditbigger.Profile.UserGenres;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/21/18.
 */

public class UserGenreViewModel extends ViewModel {
    UserGenreLiveData userGenreLiveData;

    public UserGenreViewModel(String uid){
        userGenreLiveData = new UserGenreLiveData(uid);
    }

    public UserGenreLiveData getUserGenreLiveData() {
        return userGenreLiveData;
    }
}
