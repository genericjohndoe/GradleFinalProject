package com.udacity.gradle.builditbigger.Jokes;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Comments.CommentActivity;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.Collection;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.Models.VideoInfo;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModel;
import com.udacity.gradle.builditbigger.Profile.UserPosts.OrientationControlViewModelFactory;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Search.SearchActivity;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.databinding.GenericPostBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class styles posts for recyclerview
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.JokesViewHolder> {
    private Context context;
    private List<Post> jokes;
    private boolean isUserProfile;
    private int numVideos = 0;
    private JokesViewHolder nowPlayingViewHolder = null;


    public JokesAdapter(Context context, List<Post> objects, boolean isUserProfile) {
        this.context = context;
        this.jokes = objects;
        this.isUserProfile = isUserProfile;
        setHasStableIds(true);
        Post newVideoPost = new Post("", "", System.currentTimeMillis(),
                "genre push id", "asset:///portrait_test.mp4", Constants.UID, "key", "tagline", Constants.VIDEO,
                new MetaData("video", Integer.parseInt("2") + 1, new HashMap<>()));
        jokes.add(newVideoPost);
        Post newVideoPost2 = new Post("", "", System.currentTimeMillis(),
                "genre push id", "asset:///landscape_test.mp4", Constants.UID, "keys", "tagline", Constants.VIDEO,
                new MetaData("video", Integer.parseInt("3") + 1, new HashMap<>()));
        jokes.add(newVideoPost2);
    }

    public class JokesViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        private GenericPostBinding binding;
        private LifecycleRegistry mLifecycleRegistry;
        private boolean isLiked = false;
        private Post joke;
        private OrientationControlViewModel orientationControlViewModel;

        public JokesViewHolder(GenericPostBinding binding) {
            super(binding.getRoot());
            mLifecycleRegistry = new LifecycleRegistry(this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            this.binding = binding;
            binding.taglineTextView.setOnMentionClickListener((socialView, s) -> {
                        Constants.DATABASE.child("inverseuserslist/" + s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Intent intent = new Intent(context, HilarityActivity.class);
                                intent.putExtra("uid", dataSnapshot.getValue(String.class));
                                intent.putExtra("number", 4);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                        return null;
                    }
            );
            binding.taglineTextView.setOnHashtagClickListener((socialView, s) -> {
                        Intent intent = new Intent(context, SearchActivity.class);
                        intent.putExtra("searchTerm", s);
                        intent.putExtra("position", 2);
                        context.startActivity(intent);
                        return null;
                    }
            );
            binding.optionsImageButton.setOnClickListener(view ->{
                if(isUserProfile) {
                    PopupMenu popup = new PopupMenu(context, binding.optionsImageButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.menu_post, popup.getMenu());

                    popup.setOnMenuItemClickListener(item -> {
                        if (joke != null) {
                            Constants.DATABASE.child("userposts/" + joke.getUID() + "/posts/" + joke.getPushId()).removeValue((databaseError, databaseReference) -> {
                                if (databaseError == null) {
                                    jokes.remove(joke);
                                    notifyDataSetChanged();
                                    Constants.DATABASE.child("userposts/" + joke.getUID() + "/num").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Constants.DATABASE.child("userposts/" + joke.getUID() + "/num")
                                                    .setValue(dataSnapshot.getValue(Integer.class)-1);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                        return true;
                    });
                    popup.show();
                }

            });

            binding.collectionImageButton.setOnClickListener(view -> showAddToCollectionDialog());

            orientationControlViewModel = ViewModelProviders.of((FragmentActivity) context, new OrientationControlViewModelFactory()).get(OrientationControlViewModel.class);
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        public LifecycleRegistry getmLifecycleRegistry() {
            return mLifecycleRegistry;
        }

        public void setIsLiked(boolean liked) {
            isLiked = liked;
        }

        public boolean getIsLiked() {
            return isLiked;
        }

        public void setJoke(Post joke) {
            this.joke = joke;
        }

        public Post getJoke() {
            return joke;
        }

        public void showAddToCollectionDialog(){
            List<String> collectionTitles = new ArrayList<>();
            List<String> collectionKeys = new ArrayList<>();
            Constants.DATABASE.child("usercollections/"+Constants.UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        collectionKeys.add(snapshot.getKey());
                        collectionTitles.add(snapshot.getValue(Collection.class).getTitle());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
            MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                    .customView(R.layout.dialog_add_to_collection, true)
                    .positiveText("Submit")
                    .negativeText("Cancel")
                    .onPositive((dialog, which) -> {
                        View view = dialog.getCustomView();
                        String genreTitle = ((AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView)).getText().toString();
                        if (collectionTitles.contains(genreTitle)){
                            int index = collectionTitles.indexOf(genreTitle);
                            Constants.DATABASE
                                    .child("usercollections/"+Constants.UID+"/"+collectionKeys.get(index)+"/posts/"+joke.getUID() + " " +joke.getPushId()).setValue(joke);
                        } else {
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("usercollections/" + Constants.UID).push();
                            Collection newGenre = new Collection(genreTitle, Constants.USER.getUserName(), true,
                                    System.currentTimeMillis(), Constants.USER.getUid(), db.getKey());
                            db.setValue(newGenre, (databaseError, databaseReference) -> {
                                if (databaseError == null) databaseReference.child("/posts/"+joke.getUID() + " " +joke.getPushId()).setValue(joke);
                            });
                        }
                    })
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .canceledOnTouchOutside(false)
                    .build();
            AutoCompleteTextView autoCompleteTextView = materialDialog.getCustomView().findViewById(R.id.autoCompleteTextView);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_dropdown_item_1line, collectionTitles);
            autoCompleteTextView.setAdapter(adapter);
            materialDialog.show();
        }

        public GenericPostBinding getBinding(){
            return binding;
        }

        public OrientationControlViewModel getOrientationControlViewModel() {
            return orientationControlViewModel;
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

    @Override @NonNull
    public JokesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GenericPostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.generic_post, parent, false);
        if (isUserProfile) binding.userInfoConstraintLayout.setVisibility(View.GONE);
        switch (viewType) {
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
        return new JokesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final JokesViewHolder holder, int position) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
        final Post joke = jokes.get(position);
        holder.joke = joke;

        if (joke.getType() == Constants.TEXT) {
            holder.binding.textLayout.jokeTitleTextView.setText(joke.getJokeTitle());
            holder.binding.textLayout.jokeBodyTextView.setText(joke.getJokeBody());
        } else if (joke.getType() == Constants.IMAGE) {
            Glide.with(context).load(joke.getMediaURL()).into((holder.binding.imageLayout.postImageview));
        } else if (joke.getType() == Constants.VIDEO) {
            holder.setJoke(joke);
            holder.getLifecycle().addObserver(new VideoLifeCyclerObserver(context, holder, this));
            prepareVideoPlayback(holder);
            holder.orientationControlViewModel.getOrientationLiveData().observe(holder, orientationChanged -> {
                //currently doesn't get called
                Log.i("orientation3", "viewholder observing orientation live data");
                if (orientationChanged && holder.equals(getNowPlayingViewHolder())){
                    //todo there is a lag after rotation, show loading spinner
                    holder.binding.videoLayout.postVideoView.getPlayer().setPlayWhenReady(false);
                    VideoInfo videoInfo = new VideoInfo(joke.getMediaURL(),
                            holder.binding.videoLayout.postVideoView.getPlayer().getCurrentPosition());
                    holder.orientationControlViewModel.getVideoLiveData().setValue(videoInfo);
                    Log.i("orientation4", "holder was pause, info sent: " + videoInfo.getTimeElapsed());
                } else if (!orientationChanged && holder.equals(getNowPlayingViewHolder())){
                    holder.orientationControlViewModel.getVideoLiveData().observe(holder, videoInfo -> {
                        holder.binding.videoLayout.postVideoView.getPlayer().seekTo(videoInfo.getTimeElapsed());
                        holder.binding.videoLayout.postVideoView.getPlayer().setPlayWhenReady(true);

                    });
                    Log.i("orientation3", "holder received info from dialog");
                }
            });
        } else {
            Glide.with(context).asGif().load(joke.getMediaURL())
                    .into(holder.binding.gifLayout.postGifimageview);
        }
        holder.binding.taglineTextView.setText(joke.getTagline());

        ViewHolderViewModel viewHolderViewModel = new ViewHolderViewModel(joke);

        if (!isUserProfile) {
            viewHolderViewModel.getUserNameLiveData().observe(holder, name -> {
                holder.binding.usernameTextView.setText(name);
            });
            viewHolderViewModel.getProfileImgLiveData().observe(holder, url -> {
                Glide.with(context).load(url).into(holder.binding.profileImageview);
            });
        }

        viewHolderViewModel.getNumLikesLiveData().observe(holder, num -> {
            holder.binding.likesCounterTextView.setText(num + "");
        });

        viewHolderViewModel.getNumCommentsLiveData().observe(holder, num -> {
            holder.binding.commentCounterTextView.setText(num + "");
        });

        viewHolderViewModel.getIsLikedLiveData().observe(holder, aBoolean -> {
            if (aBoolean) {
                Glide.with(context.getApplicationContext()).load(R.drawable.hilarity_mask_like).into(holder.binding.favoriteImageButton);
            } else {
                Glide.with(context.getApplicationContext()).load(R.drawable.hilarity_mask_unlike).into(holder.binding.favoriteImageButton);
            }
            holder.setIsLiked(aBoolean);
        });

        holder.binding.timeDateTextView.setText(Constants.formattedTimeString(context, joke.getTimeStamp()));

        holder.binding.favoriteImageButton.setOnClickListener(view -> {
            final String path = "userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/likes/list/" + Constants.UID;
            if (holder.getIsLiked()) {
                Constants.DATABASE.child(path).removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        Glide.with(context).load(R.drawable.hilarity_mask_unlike).into(holder.binding.favoriteImageButton);
                        Constants.DATABASE.child("userlikes/" + Constants.UID + "/list/" + joke.getUID() + " " + joke.getPushId()).removeValue();
                    }
                });
            } else {
                Constants.DATABASE.child(path).setValue(true, (databaseError, databaseReference) -> {
                    if (databaseError == null){
                        Glide.with(context).load(R.drawable.hilarity_mask_like).into(holder.binding.favoriteImageButton);
                        Constants.DATABASE.child("userlikes/" + Constants.UID + "/list/" + joke.getUID() + " " + joke.getPushId()).setValue(joke);
                    }
                });
            }
        });

        holder.binding.commentImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("uid", joke.getUID());
            intent.putExtra("pushId", joke.getPushId());
            context.startActivity(intent);
            }
        );
    }

    @Override
    public void onViewAttachedToWindow(@NonNull JokesViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        if (holder.getJoke() != null && holder.getJoke().getType() == Constants.VIDEO) {
            prepareVideoPlayback(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull JokesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        if (holder.getJoke().getType() == Constants.VIDEO){
            //holder.orientationControlViewModel.getNumVideosLiveData().setValue(--numVideos);
            Log.i("numMovies", numVideos+"");
        }
    }

    @Override
    public void onViewRecycled(@NonNull JokesViewHolder holder) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public void prepareVideoPlayback(JokesViewHolder holder) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Hilarity"), bandwidthMeter);
        SimpleCache cache = new SimpleCache(context.getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024^2*100));
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (holder.binding.videoLayout.postVideoView.getPlayer() != null) {
            holder.binding.videoLayout.postVideoView.getPlayer().prepare(new ExtractorMediaSource(Uri.parse(holder.getJoke().getMediaURL()),
                    dataSourceFactory, extractorsFactory, null, null), false, false);
        }
    }

    public void setJokes(List<Post> jokes) {
        this.jokes = jokes;
        notifyDataSetChanged();
    }

    public void setNowPlayingViewHolder(JokesViewHolder nowPlayingViewHolder) {
        this.nowPlayingViewHolder = nowPlayingViewHolder;
    }

    public JokesViewHolder getNowPlayingViewHolder() {
        return nowPlayingViewHolder;
    }
}
