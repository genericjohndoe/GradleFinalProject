package com.udacity.gradle.builditbigger.Profile.UserGenres;

import android.arch.lifecycle.ViewModel;

/**
 * UserGenreViewModel class provides fragment with user generated genres
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
