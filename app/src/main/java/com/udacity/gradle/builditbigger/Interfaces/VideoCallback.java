package com.udacity.gradle.builditbigger.Interfaces;

/**
 * Created by joeljohnson on 12/27/17.
 */

public interface VideoCallback {

    void getVideoInfo(boolean started, int position);

    void onNewVideoPost(long id);

    void onVideoPostRecycled(long id);

    void setCurrentlyPlaying(long id);
}
