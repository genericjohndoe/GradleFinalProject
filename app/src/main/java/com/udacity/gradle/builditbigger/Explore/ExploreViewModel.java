package com.udacity.gradle.builditbigger.Explore;

import android.arch.lifecycle.ViewModel;
import android.content.Context;

/**
 * class provides all posts to fragment
 */

public class ExploreViewModel extends ViewModel {

    private ExploreLiveData exploreLiveData;

    public ExploreViewModel(Context context){
        exploreLiveData = new ExploreLiveData(context);
    }

    public ExploreLiveData getExploreLiveData() {
        return exploreLiveData;
    }
}
