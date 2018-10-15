package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.MutableLiveData;

import com.udacity.gradle.builditbigger.models.VideoInfo;

/**
 * Created by joeljohnson on 4/25/18.
 */

public class VideoInfoLiveData extends MutableLiveData<VideoInfo> {

    private static  VideoInfoLiveData videoInfoLiveData;

    public static VideoInfoLiveData getInstance() {
        if (videoInfoLiveData == null) videoInfoLiveData = new VideoInfoLiveData();
        return videoInfoLiveData;
    }
}
