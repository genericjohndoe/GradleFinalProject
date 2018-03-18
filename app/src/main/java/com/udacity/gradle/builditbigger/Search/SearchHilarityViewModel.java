package com.udacity.gradle.builditbigger.Search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 3/16/18.
 */

public class SearchHilarityViewModel extends ViewModel {
    private static SearchHilarityViewModel searchHilarityViewModel;

    private SearchHilarityViewModel(){}

    public static SearchHilarityViewModel getInstance(){
        if (searchHilarityViewModel == null){
            searchHilarityViewModel = new SearchHilarityViewModel();
        }
        return searchHilarityViewModel;
    }

    private MutableLiveData<String> searchQuery;

    public MutableLiveData<String> getSearchQuery() {
        if (searchQuery == null){
            searchQuery = new MutableLiveData<>();
        }
        return searchQuery;
    }

}
