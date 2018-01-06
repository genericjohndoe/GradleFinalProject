package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.DefaultPlaybackController;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;

/**
 * Created by joeljohnson on 12/26/17.
 */

public class VideoLifeCyclerObserver implements LifecycleObserver {

    MediaSessionCompat mMediaSession;
    PlaybackStateCompat.Builder mStateBuilder;
    AppCompatActivity activity;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    ExoPlayer.ExoPlayerComponent rv;
    MediaSessionConnector mediaSessionConnector;

    public VideoLifeCyclerObserver(AppCompatActivity activity, SimpleExoPlayerView playerView, ExoPlayer.ExoPlayerComponent rv){
        this.activity = activity;
        this.playerView = playerView;
        this.activity.getLifecycle().addObserver(this);
        this.rv = rv;
        player.addListener(new ExoEventPlayer(rv));
        Log.i("Hoe8","video lco created");
        Log.i("Hoe8", "2 "+(this.rv == null));
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setUp(LifecycleOwner lifecycleOwner){
        // Create a MediaSessionCompat
        Log.i("Hoe8", "lco setup called");
        mMediaSession = new MediaSessionCompat(activity, "this");

        // Create a MediaControllerCompat
        MediaControllerCompat mediaController =
                new MediaControllerCompat(activity, mMediaSession);

        MediaControllerCompat.setMediaController(activity, mediaController);

        mediaSessionConnector =
                new MediaSessionConnector(mMediaSession, new PlayBackController());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void startPlayer(LifecycleOwner lifecycleOwner){
        Log.i("Hoe8", "startPlayer called");
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);
        Log.i("Hoe8", "3 "+(this.rv == null));
        //player.addListener(new ExoEventPlayer(rv));
        playerView.setPlayer(player);
        mediaSessionConnector.setPlayer(player, null,null);
        mMediaSession.setActive(true);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void tearDown(LifecycleOwner lifecycleOwner){
        Log.i("Hoe8", "tearDown called");
        player.stop();
        player.release();
        player.sendMessages(new ExoPlayer.ExoPlayerMessage(rv,1,player.getContentPosition()));
        mMediaSession.setActive(false);
    }

    public class PlayBackController extends DefaultPlaybackController{
        @Override
        public void onPause(Player player) {
            Log.i("Hoe8", "onPause called");
            ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
            super.onPause(player);
        }

        @Override
        public void onPlay(Player player) {
            Log.i("Hoe8", "MediaSession callback play called 2");
            mMediaSession.setActive(true);
            ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(true);
            ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(true);
            super.onPlay(player);
        }

        @Override
        public void onStop(Player player) {
            Log.i("Hoe8", "onStop called");
            mMediaSession.setActive(false);
            ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
            ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(false);
            super.onStop(player);
        }


    }
}
