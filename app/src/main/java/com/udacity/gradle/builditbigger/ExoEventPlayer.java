package com.udacity.gradle.builditbigger;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;

/**
 * class reacts to changes in state of video
 */

public class ExoEventPlayer implements Player.EventListener {

    private JokesAdapter jokesAdapter;
    private JokesAdapter.JokesViewHolder viewHolder;

    public ExoEventPlayer(JokesAdapter jokesAdapter, JokesAdapter.JokesViewHolder viewHolder){
        this.jokesAdapter = jokesAdapter;
        this.viewHolder = viewHolder;
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

        if (playWhenReady && playbackState == Player.STATE_READY){
            Log.i("Hoe8", "play called");
            if (jokesAdapter.getNowPlayingViewHolder() == null) {
                jokesAdapter.setNowPlayingViewHolder(viewHolder);
            } else {
                jokesAdapter.getNowPlayingViewHolder().getBinding().videoLayout.postVideoView.getPlayer().setPlayWhenReady(false);
                jokesAdapter.setNowPlayingViewHolder(viewHolder);
            }
        } else {
            jokesAdapter.setNowPlayingViewHolder(null);
        }

    }

    @Override
    public void onPositionDiscontinuity() {}

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

}
