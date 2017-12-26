package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

/**
 * Created by joeljohnson on 12/26/17.
 */

public class VideoLifeCyclerObserver implements LifecycleObserver {

    MediaSessionCompat mMediaSession;
    PlaybackStateCompat.Builder mStateBuilder;
    AppCompatActivity activity;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;

    public VideoLifeCyclerObserver(AppCompatActivity activity, SimpleExoPlayerView playerView){
        this.activity = activity;
        this.playerView = playerView;
        this.activity.getLifecycle().addObserver(this);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setUp(){
        // Create a MediaSessionCompat
        mMediaSession = new MediaSessionCompat(activity, "this");

        // Enable callbacks from MediaButtons and TransportControls
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        //mMediaSession.setState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                mMediaSession.setActive(true);
            }

            @Override
            public void onPause() {
                super.onPause();
            }

            @Override
            public void onStop() {
                super.onStop();
                mMediaSession.setActive(false);
            }
        });

        // Create a MediaControllerCompat
        MediaControllerCompat mediaController =
                new MediaControllerCompat(activity, mMediaSession);

        MediaControllerCompat.setMediaController(activity, mediaController);

        //Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);


// 2. Create the player
        player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);
        playerView.setPlayer(player);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void tearDown(){
        player.release();
    }
}
