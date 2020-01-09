package com.udacity.gradle.builditbigger.explore;

import androidx.lifecycle.ViewModel;

/**
 * class provides all posts to fragment
 */

public class ExploreViewModel extends ViewModel {

    private ExploreLiveData exploreLiveData;

    public ExploreViewModel(){
        exploreLiveData = new ExploreLiveData();
    }

    public ExploreLiveData getExploreLiveData() {
        return exploreLiveData;
    }
}
