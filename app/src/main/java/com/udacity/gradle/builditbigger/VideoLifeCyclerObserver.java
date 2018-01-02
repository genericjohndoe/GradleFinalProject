package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.udacity.gradle.builditbigger.Jokes.JokesAdapter;

/**
 * Created by joeljohnson on 12/26/17.
 */

public class VideoLifeCyclerObserver implements LifecycleObserver, ExoPlayer.EventListener, MediaSessionConnector.PlaybackController {

    MediaSessionCompat mMediaSession;
    PlaybackStateCompat.Builder mStateBuilder;
    AppCompatActivity activity;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    ExoPlayer.ExoPlayerComponent rv;

    public VideoLifeCyclerObserver(AppCompatActivity activity, SimpleExoPlayerView playerView, ExoPlayer.ExoPlayerComponent rv){
        this.activity = activity;
        this.playerView = playerView;
        this.activity.getLifecycle().addObserver(this);
        this.rv = rv;
        Log.i("Hoe8","video lco created");
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setUp(LifecycleOwner lifecycleOwner){
        // Create a MediaSessionCompat
        Log.i("Hoe8", "lco setup called");
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
        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller
        mMediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                Log.i("Hoe8", "MediaSession callback play called");
                mMediaSession.setActive(true);
                ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(true);
                ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(true);

            }

            @Override
            public void onPause() {
                super.onPause();
                ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
            }

            @Override
            public void onStop() {
                super.onStop();
                mMediaSession.setActive(false);
                ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
                ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(false);
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

        MediaSessionConnector mediaSessionConnector =
                new MediaSessionConnector(mMediaSession, this);

        mediaSessionConnector.setPlayer(player, null,null );


    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void tearDown(LifecycleOwner lifecycleOwner){
        player.stop();
        player.release();
        player.sendMessages(new ExoPlayer.ExoPlayerMessage(rv,1,player.getContentPosition()));
    }

    @Override
    public void onPlayerStateChanged(boolean b, int i) {

        if((i == ExoPlayer.STATE_READY) && b ){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    player.getCurrentPosition(), 1f);
            ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(true);
            ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(true);
        } else if((i == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    player.getCurrentPosition(), 1f);
            ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
        } else if (i == ExoPlayer.STATE_ENDED){
            ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
            ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(false);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object o) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {

    }

    @Override
    public void onLoadingChanged(boolean b) {

    }


    @Override
    public void onPlayerError(ExoPlaybackException e) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onFastForward(Player player) {

    }

    @Override
    public void onPause(Player player) {
        ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
    }

    @Override
    public void onPlay(Player player) {
        Log.i("Hoe8", "MediaSession callback play called 2");
        mMediaSession.setActive(true);
        ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(true);
        ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(true);
    }

    @Override
    public void onRewind(Player player) {

    }

    @Override
    public void onSeekTo(Player player, long position) {

    }

    @Override
    public void onStop(Player player) {
        mMediaSession.setActive(false);
        ((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
        ((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(false);
    }

    @Override
    public long getSupportedPlaybackActions(@Nullable Player player) {
        return 0;
    }
}
