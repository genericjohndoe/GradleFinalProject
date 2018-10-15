package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.udacity.gradle.builditbigger.jokes.JokesAdapter;

/**
 * class reacts to changes in state of video
 */

public class ExoEventPlayer implements Player.EventListener {

    private JokesAdapter jokesAdapter;
    private JokesAdapter.JokesViewHolder viewHolder;
    private Context context;

    public ExoEventPlayer(JokesAdapter jokesAdapter, JokesAdapter.JokesViewHolder viewHolder, Context context){
        this.jokesAdapter = jokesAdapter;
        this.viewHolder = viewHolder;
        this.context = context;
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}

    @Override
    public void onRepeatModeChanged(int repeatMode) {}

    @Override
    public void onLoadingChanged(boolean isLoading) {}

    @Override
    public void onPlayerError(ExoPlaybackException error) {}

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        Log.i("orientation3", "exoeventplayer onplayerstatechanged playwhenready = " +playWhenReady);
        Log.i("orientation3", "exoplayer onplayerstatechanged playbackstate = " +playbackState);
        Log.i("orientation3", viewHolder.toString() + " being worked on in onplayerstatechanged");

        if (playWhenReady && playbackState == Player.STATE_READY){
            viewHolder.getOrientationControlViewModel().getVideoPlayingMutableLiveData().setValue(true);
            if (jokesAdapter.getNowPlayingViewHolder() == null) {
                jokesAdapter.setNowPlayingViewHolder(viewHolder);
                Log.i("orientation3", "exoplayer onplayerstatechanged now player viewholder set from null");
            } else if (!jokesAdapter.getNowPlayingViewHolder().equals(viewHolder)) {
                jokesAdapter.getNowPlayingViewHolder().getBinding().videoLayout.postVideoView.getPlayer().setPlayWhenReady(false);
                jokesAdapter.setNowPlayingViewHolder(viewHolder);
                Log.i("orientation3", "exoplayer onplayerstatechanged now player viewholder replaced");
            } else if (jokesAdapter.getNowPlayingViewHolder().equals(viewHolder)){
                //jokesAdapter.prepareVideoPlayback(viewHolder);
            }
        } else if (playbackState == Player.STATE_ENDED){
            jokesAdapter.setNowPlayingViewHolder(null);
            viewHolder.getOrientationControlViewModel().getVideoPlayingMutableLiveData().setValue(false);
            Log.i("orientation3", "exoplayer onplayerstatechanged video ended now player viewholder set to null");
        }
    }

    @Override
    public void onPositionDiscontinuity() {}

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

}
