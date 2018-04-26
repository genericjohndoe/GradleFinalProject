package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.Models.VideoInfo;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModel;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModelFactory;

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

        OrientationControlViewModel orientationControlViewModel = ViewModelProviders.of((FragmentActivity) context, new OrientationControlViewModelFactory()).get(OrientationControlViewModel.class);

        if (playWhenReady && playbackState == Player.STATE_READY){
            if (jokesAdapter.getNowPlayingViewHolder() == null) {
                jokesAdapter.setNowPlayingViewHolder(viewHolder);
            } else {
                jokesAdapter.getNowPlayingViewHolder().getBinding().videoLayout.postVideoView.getPlayer().setPlayWhenReady(false);
                jokesAdapter.setNowPlayingViewHolder(viewHolder);
            }
        } else if (playbackState == Player.STATE_ENDED){
            jokesAdapter.setNowPlayingViewHolder(null);
            orientationControlViewModel.getVideoPlayingMutableLiveData().setValue(false);
        }
        orientationControlViewModel.getVideoPlayingMutableLiveData().setValue(jokesAdapter.getNowPlayingViewHolder() != null);
        //Log.i("orientation3", "EventPlayer, tells listeners where or not adapter's VH is null, VH = " + (jokesAdapter.getNowPlayingViewHolder() != null));
    }

    @Override
    public void onPositionDiscontinuity() {}

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {}

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

}
