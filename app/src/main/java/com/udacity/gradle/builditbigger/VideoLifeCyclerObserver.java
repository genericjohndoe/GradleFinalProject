package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.udacity.gradle.builditbigger.jokes.JokesAdapter;
import com.udacity.gradle.builditbigger.messaging.transcripts.MessagesAdapter;

/**
 * class controls video playback
 */

public class VideoLifeCyclerObserver implements LifecycleObserver {

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private MediaSessionConnector mediaSessionConnector;
    private Context context;
    private long position;
    private RecyclerView.ViewHolder viewHolder;
    private RecyclerView.Adapter jokesAdapter;

    public VideoLifeCyclerObserver(Context context, RecyclerView.ViewHolder viewHolder, RecyclerView.Adapter jokesAdapter){
        if (viewHolder instanceof JokesAdapter.JokesViewHolder){
            this.playerView = ((JokesAdapter.JokesViewHolder) viewHolder).getBinding().videoLayout.postVideoView;
        } else {
            this.playerView =  ((MessagesAdapter.SentVideoMessagesViewHolder) viewHolder).getExoPlayerView();
        }

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
        mediaSessionConnector = new MediaSessionConnector(mMediaSession);
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

        if ((viewHolder instanceof JokesAdapter.JokesViewHolder) && jokesAdapter != null && viewHolder != null) player.addListener(new ExoEventPlayer((JokesAdapter) jokesAdapter, (JokesAdapter.JokesViewHolder) viewHolder, context));
        playerView.setPlayer(player);
        mediaSessionConnector.setPlayer(player);
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

    /*public class PlayBackController extends DefaultPlaybackController{
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
    }*/
}
