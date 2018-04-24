package com.udacity.gradle.builditbigger.Profile.UserPosts;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 4/8/18.
 */

public class OrientationControlViewModel extends ViewModel {
    private static  MutableLiveData<Integer> numVideosLiveData;
    private static OrientationControlViewModel orientationControlViewModel;

    public static OrientationControlViewModel getInstance() {
        if (orientationControlViewModel == null) orientationControlViewModel = new OrientationControlViewModel();
        return orientationControlViewModel;
    }

    public MutableLiveData<Integer> getNumVideosLiveData() {
        if (numVideosLiveData == null) numVideosLiveData = new MutableLiveData<>();
        return numVideosLiveData;
    }
}
