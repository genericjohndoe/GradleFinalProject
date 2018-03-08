package com.udacity.gradle.builditbigger.Jokes;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.socialview.SocialView;
import com.hendraanggrian.widget.SocialTextView;
import com.udacity.gradle.builditbigger.Comments.CommentFragment;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Database.HilarityUserDatabase;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Interfaces.VideoCallback;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.databinding.GenericPostBinding;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/**
 * Created by joeljohnson on 7/17/17.
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.JokesViewHolder> {
    Context context;
    List<Joke> jokes;
    int id = 1;
    VideoCallback vc;
    boolean isUserProfile;
    private LifecycleRegistry mLifecycleRegistry;


    public JokesAdapter(Context context, List<Joke> objects, VideoCallback vc, boolean isUserProfile) {
        this.context = context;
        this.jokes = objects;
        this.vc = vc;
        this.isUserProfile = isUserProfile;
        setHasStableIds(true);
    }

    public class JokesViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        GenericPostBinding binding;
        SocialTextView socialTextView;
        private LifecycleRegistry mLifecycleRegistry;
        private boolean isLiked = false;
        private Joke joke;

        public JokesViewHolder(GenericPostBinding binding) {
            super(binding.getRoot());
            mLifecycleRegistry = new LifecycleRegistry(this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            this.binding = binding;
            this.socialTextView = (SocialTextView) binding.getRoot().findViewById(R.id.tagline_textView);
            socialTextView.setOnMentionClickListener(new Function2<SocialView, String, Unit>() {
                @Override
                public Unit invoke(SocialView socialView, String s) {
                    //todo get uid from username
                    Constants.changeFragment(R.id.hilarity_content_frame,Profile.newInstance("uid"));
                    return null;
                }
            });
            socialTextView.setOnHashtagClickListener(new Function2<SocialView, String, Unit>() {
                @Override
                public Unit invoke(SocialView socialView, String s) {
                    //todo send to search page
                    return null;
                }
            });
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        public LifecycleRegistry getmLifecycleRegistry(){
            return mLifecycleRegistry;
        }

        public void setIsLiked(boolean liked){
            isLiked = liked;
        }

        public boolean getIsLiked(){
            return isLiked;
        }

        public void setJoke(Joke joke){
            this.joke = joke;
        }

        public Joke getJoke(){
            return joke;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return jokes.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    @Override
    public JokesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GenericPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.generic_post, parent, false);
        if (isUserProfile) binding.userInfoConstraintLayout.setVisibility(View.GONE);
        switch  (viewType){
            case Constants.TEXT:
                binding.imageLayout.imageRootLayout.setVisibility(View.GONE);
                binding.videoLayout.videoFramelayout.setVisibility(View.GONE);
                binding.gifLayout.gifRootlayout.setVisibility(View.GONE);
                return new JokesViewHolder(binding);
            case Constants.IMAGE:
                binding.textLayout.textRootLayout.setVisibility(View.GONE);
                binding.videoLayout.videoFramelayout.setVisibility(View.GONE);
                binding.gifLayout.gifRootlayout.setVisibility(View.GONE);
                return new JokesViewHolder(binding);
            case Constants.VIDEO:
                binding.textLayout.textRootLayout.setVisibility(View.GONE);
                binding.imageLayout.imageRootLayout.setVisibility(View.GONE);
                binding.gifLayout.gifRootlayout.setVisibility(View.GONE);
                return new JokesViewHolder(binding);
            case Constants.GIF:
                binding.textLayout.textRootLayout.setVisibility(View.GONE);
                binding.imageLayout.imageRootLayout.setVisibility(View.GONE);
                binding.videoLayout.videoFramelayout.setVisibility(View.GONE);
                return new JokesViewHolder(binding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final JokesViewHolder holder, int position) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
        final Joke joke = jokes.get(position);


        if (joke.getType() == Constants.TEXT) {
            holder.binding.textLayout.jokeTitleTextView.setText(joke.getJokeTitle());
            holder.binding.textLayout.jokeBodyTextView.setText(joke.getJokeBody());
        } else if (joke.getType() == Constants.IMAGE) {
            Glide.with(context).load(joke.getMediaURL()).into((holder.binding.imageLayout.postImageview));
        } else if (joke.getType() == Constants.VIDEO) {
            holder.setJoke(joke);
            holder.getLifecycle().addObserver(new VideoLifeCyclerObserver(context, holder.binding.videoLayout.postVideoView));
            prepareVideoPlayback(holder);
        } else {
            Glide.with(context).asGif().load(joke.getMediaURL())
                    .into(holder.binding.gifLayout.postGifimageview);
        }
        holder.socialTextView.setText(joke.getTagline());

        ViewHolderViewModel viewHolderViewModel = new ViewHolderViewModel(joke);

        viewHolderViewModel.getUserNameLiveData().observe(holder, name -> {
            holder.binding.usernameTextView.setText(name);
        });

        viewHolderViewModel.getProfileImgLiveData().observe(holder, url -> {
            Glide.with(context).load(url).into(holder.binding.profileImageview);
        });

        viewHolderViewModel.getNumLikesLiveData().observe(holder, num -> {
            holder.binding.likesCounterTextView.setText(num+"");
        });

        viewHolderViewModel.getNumCommentsLiveData().observe(holder, num -> {
            holder.binding.commentCounterTextView.setText(num+"");
        });

        viewHolderViewModel.getIsLikedLiveData().observe(holder, aBoolean -> {
            if (aBoolean){
                Glide.with(context).load(R.drawable.ic_favorite_black_24dp).into(holder.binding.favoriteImageButton);
            } else{
                Glide.with(context).load(R.drawable.ic_favorite_border_black_24dp).into(holder.binding.favoriteImageButton);
            }
            holder.setIsLiked(aBoolean);
            Log.i("likes", "automatic liked value set "+ aBoolean);
        });

        holder.binding.timeDateTextView.setText(Constants.formattedTimeString(context, joke.getTimeStamp()));

        holder.binding.favoriteImageButton.setOnClickListener(view -> {
                final String path = "userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/likes/list/" + Constants.UID;
                if (holder.getIsLiked()){
                    Constants.DATABASE.child(path).removeValue();
                    Glide.with(context).load(R.drawable.ic_favorite_border_black_24dp).into(holder.binding.favoriteImageButton);
                } else {
                    Constants.DATABASE.child(path).setValue(true);
                    Glide.with(context).load(R.drawable.ic_favorite_black_24dp).into(holder.binding.favoriteImageButton);
                }
        });

        holder.binding.commentImageButton.setOnClickListener(view -> {
                Constants.changeFragment(R.id.hilarity_content_frame,CommentFragment.newInstance(joke.getUID(),joke.getPushId()),(AppCompatActivity) context);
            }
        );
    }

    public class TextPostViewHolder extends JokesViewHolder{
        GenericPostBinding binding;

        public TextPostViewHolder(GenericPostBinding bind) {
            super(bind);
            this.binding = bind;
        }

    }

    public class ImagePostViewHolder extends JokesViewHolder{
        GenericPostBinding binding;

        public ImagePostViewHolder(GenericPostBinding binding){
            super(binding);
            this.binding = binding;
        }
    }

    public class VideoPostViewHolder extends JokesViewHolder {
        VideoLifeCyclerObserver vlco;
        long playPosition = 0L;
        boolean isPlaying = false;
        boolean hasStarted = false;
        long id;
        GenericPostBinding binding;

        //todo when video is started check to see if another video is playing

        public VideoPostViewHolder(GenericPostBinding binding){
            super(binding);
            this.binding = binding;
            vc.onNewVideoPost(getItemId());
            Log.i("Hoe8", "vid post created");
            setVlco();

        }

        public void setVlco(){
            Log.i("Hoe8",""+(this==null));
            //vlco = new VideoLifeCyclerObserver( context, binding.videoLayout.postVideoView, this);
        }

        public SimpleExoPlayerView getPost(){
            return binding.videoLayout.postVideoView;
        }

        public long getPlayerPosition(){
            return playPosition;
        }

        public void setPlayerPosition(long position){
            playPosition = position;
        }

        public void setIsPlaying(boolean playing){
            Log.i("Hoe8", "VideoPostViewHolder.setIsPlaying called");
            if (playing) vc.setCurrentlyPlaying((int) getItemId());
            Log.i("Hoe8","item id " + getItemId());
            isPlaying = playing;
        }

        public boolean isPlaying(){
            return isPlaying;
        }

        public boolean hasStarted(){
            return hasStarted;
        }

        public void setHasStarted(boolean started){
            Log.i("Hoe8", "vh hasStarted called");
            hasStarted = started;
        }
    }

    public class GifPostViewHolder extends JokesViewHolder{
        GenericPostBinding binding;

        public GifPostViewHolder(GenericPostBinding bind){
            super(bind);
            this.binding = bind;
        }
    }

    @Override
    public void onViewAttachedToWindow(JokesViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        if (holder.getJoke().getType() == Constants.VIDEO){
            prepareVideoPlayback(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(JokesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onViewRecycled(JokesViewHolder holder) {
        Log.i("Hoe8","onViewRecycled");
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        /*if (holder instanceof VideoPostViewHolder){
            vc.onVideoPostRecycled(holder.getItemId());
        }*/
        super.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public void prepareVideoPlayback(JokesViewHolder holder){
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Hilarity"), bandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (holder.binding.videoLayout.postVideoView.getPlayer() != null) {
            holder.binding.videoLayout.postVideoView.getPlayer().prepare(new ExtractorMediaSource(Uri.parse(holder.getJoke().getMediaURL()),
                    dataSourceFactory, extractorsFactory, null, null), false, false);
            Log.i("Video playback","position for view holder ");
            Log.i("Video playback", "holder " + holder.binding.videoLayout.postVideoView.getPlayer().toString());
        } else {
            Log.i("Video playback","getPlayer() return null");
        }
    }
}
