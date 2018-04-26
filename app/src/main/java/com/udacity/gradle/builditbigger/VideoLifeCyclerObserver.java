package com.udacity.gradle.builditbigger;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

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
 * class controls video playback
 */

public class VideoLifeCyclerObserver implements LifecycleObserver {

    MediaSessionCompat mMediaSession;
    PlaybackStateCompat.Builder mStateBuilder;
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    MediaSessionConnector mediaSessionConnector;
    Context context;
    long position;
    JokesAdapter.JokesViewHolder viewHolder;
    JokesAdapter jokesAdapter;

    public VideoLifeCyclerObserver(Context context, JokesAdapter.JokesViewHolder viewHolder, JokesAdapter jokesAdapter){
        this.playerView = viewHolder.getBinding().videoLayout.postVideoView;
        this.context = context;
        this.jokesAdapter = jokesAdapter;
        this.viewHolder = viewHolder;
    }

    public VideoLifeCyclerObserver(Context context, SimpleExoPlayerView playerView){
        this.playerView = playerView;
        this.context = context;
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void setUp(LifecycleOwner lifecycleOwner){
        // Create a MediaSessionCompat
        Log.i("Video playback", "setup called");
        mMediaSession = new MediaSessionCompat(context, "this");
        // Create a MediaControllerCompat
        MediaControllerCompat mediaController = new MediaControllerCompat(context, mMediaSession);
        MediaControllerCompat.setMediaController((AppCompatActivity) context, mediaController);
        mediaSessionConnector = new MediaSessionConnector(mMediaSession, new PlayBackController());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startPlayer(LifecycleOwner lifecycleOwner){
        Log.i("Video playback", "startPlayer called");
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        if (jokesAdapter != null && viewHolder != null) player.addListener(new ExoEventPlayer(jokesAdapter, viewHolder, context));
        playerView.setPlayer(player);
        mediaSessionConnector.setPlayer(player, null,null);
        mMediaSession.setActive(true);
        Log.i("Video playback", "startPlayer finished");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void resume(LifecycleOwner lifecycleOwner){
        player.seekTo(position);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void tearDown(LifecycleOwner lifecycleOwner){
        Log.i("Video playback", "tearDown called");
        player.stop();
        position = player.getCurrentPosition();
        player.release();
        //player.sendMessages(new ExoPlayer.ExoPlayerMessage(rv,1,player.getContentPosition()));
        mMediaSession.setActive(false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void destroy(LifecycleOwner lifecycleOwner){
        Log.i("Video playback", "destroy called");
        player.release();
    }

    public class PlayBackController extends DefaultPlaybackController{
        @Override
        public void onPause(Player player) {
            Log.i("Hoe8", "onPause called");
            //((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
            super.onPause(player);
        }

        @Override
        public void onPlay(Player player) {
            Log.i("Hoe8", "MediaSession callback play called 2");
            mMediaSession.setActive(true);
            //((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(true);
            //((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(true);
            super.onPlay(player);
        }

        @Override
        public void onStop(Player player) {
            Log.i("Hoe8", "onStop called");
            mMediaSession.setActive(false);
            //((JokesAdapter.VideoPostViewHolder) rv).setIsPlaying(false);
            //((JokesAdapter.VideoPostViewHolder) rv).setHasStarted(false);
            super.onStop(player);
        }
    }
}
