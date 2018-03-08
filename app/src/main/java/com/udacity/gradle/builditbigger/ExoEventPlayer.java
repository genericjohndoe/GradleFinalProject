package com.udacity.gradle.builditbigger;

import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;

/**
 * Created by joeljohnson on 1/4/18.
 */

public class ExoEventPlayer implements Player.EventListener {

    //private ExoPlayer.ExoPlayerComponent rv;

    public ExoEventPlayer(){
        /*this.rv = rv;*/
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
            /*if (rv !=null) {
               //  ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(true);
               // ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(true);
            }*/
        } else if (!playWhenReady && playbackState == Player.STATE_READY){
            Log.i("Hoe8", "onPause called");
            //if (rv != null)((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
        } else if (playbackState == Player.STATE_ENDED){
            /*if (rv != null) {
                ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
                ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(false);
            }*/
        }

    }

    @Override
    public void onPositionDiscontinuity() {}

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

}
