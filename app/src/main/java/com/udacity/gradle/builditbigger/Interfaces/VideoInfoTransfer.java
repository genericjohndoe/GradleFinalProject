package com.udacity.gradle.builditbigger.Interfaces;

import com.udacity.gradle.builditbigger.Models.VideoInfo;

/**
 * Created by joeljohnson on 4/25/18.
 */

public interface VideoInfoTransfer {
    VideoInfo receiveInfo(VideoInfo info);
    VideoInfo sendInfo(VideoInfo info);
}