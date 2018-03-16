package com.udacity.gradle.builditbigger.Search;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 3/16/18.
 */

public class SearchUserViewModel extends ViewModel {

    private SearchUserLiveData searchUserLiveData = new SearchUserLiveData();

    public SearchUserLiveData getSearchUserLiveData() {
        return searchUserLiveData;
    }
}
