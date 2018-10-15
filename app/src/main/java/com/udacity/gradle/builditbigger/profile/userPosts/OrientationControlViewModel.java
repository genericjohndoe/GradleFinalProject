package com.udacity.gradle.builditbigger.profile.userPosts;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.gradle.builditbigger.models.VideoInfo;

/**
 * Created by joeljohnson on 4/8/18.
 */

public class OrientationControlViewModel extends ViewModel {
    private static  MutableLiveData<VideoInfo> videoLiveData;
    private static MutableLiveData<Boolean> orientationLiveData;
    private static MutableLiveData<Boolean> videoPlayingMutableLiveData;
    private static OrientationControlViewModel orientationControlViewModel;

    public static OrientationControlViewModel getInstance() {
        if (orientationControlViewModel == null) orientationControlViewModel = new OrientationControlViewModel();
        return orientationControlViewModel;
    }

    public MutableLiveData<VideoInfo> getVideoLiveData() {
        if (videoLiveData == null) videoLiveData = new MutableLiveData<>();
        return videoLiveData;
    }

    public MutableLiveData<Boolean> getOrientationLiveData(){
        if (orientationLiveData == null) orientationLiveData = new MutableLiveData<>();
        return orientationLiveData;
    }

    public MutableLiveData<Boolean> getVideoPlayingMutableLiveData() {
        if (videoPlayingMutableLiveData == null) videoPlayingMutableLiveData = new MutableLiveData<>();
        return videoPlayingMutableLiveData;
    }
}
