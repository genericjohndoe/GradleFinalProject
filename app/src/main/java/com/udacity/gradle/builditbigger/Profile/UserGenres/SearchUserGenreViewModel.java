package com.udacity.gradle.builditbigger.Profile.UserGenres;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 1/21/18.
 */

public class SearchUserGenreViewModel extends ViewModel {
    private SearchUserGenreLiveData searchUserGenreLiveData;
    public SearchUserGenreViewModel(String uid, String[] tags){
        searchUserGenreLiveData = new SearchUserGenreLiveData(uid, tags);
    }

    public SearchUserGenreLiveData getSearchUserGenreLiveData() {
        return searchUserGenreLiveData;
    }
}
