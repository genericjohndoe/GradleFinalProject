package com.udacity.gradle.builditbigger.Interfaces;

/**
 * DEPRECATED
 */

public interface VideoCallback {

    void getVideoInfo(boolean started, int position);

    void onNewVideoPost(long id);

    void onVideoPostRecycled(long id);

    void setCurrentlyPlaying(long id);
}
