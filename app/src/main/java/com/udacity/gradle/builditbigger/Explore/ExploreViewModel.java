package com.udacity.gradle.builditbigger.Explore;

import android.arch.lifecycle.ViewModel;

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
