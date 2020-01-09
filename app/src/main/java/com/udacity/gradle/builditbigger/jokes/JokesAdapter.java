package com.udacity.gradle.builditbigger.jokes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
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
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.comments.CommentActivity;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.GenericPostBinding;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.Collection;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.models.VideoInfo;
import com.udacity.gradle.builditbigger.newPost.NewPostActivity2;
import com.udacity.gradle.builditbigger.newPost.audioMediaPost.AudioMediaPostSubmissionActivity;
import com.udacity.gradle.builditbigger.newPost.visualMediaPost.VisualMediaPostSubmissionActivity;
import com.udacity.gradle.builditbigger.post.PostActivity;
import com.udacity.gradle.builditbigger.profile.userPosts.OrientationControlViewModel;
import com.udacity.gradle.builditbigger.profile.userPosts.OrientationControlViewModelFactory;
import com.udacity.gradle.builditbigger.reportPost.ReportActivity;
import com.udacity.gradle.builditbigger.search.SearchActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Class styles posts for recyclerview
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.JokesViewHolder> {
    private Context context;
    private List<Post> jokes;
    private boolean isUserProfile;
    private JokesViewHolder nowPlayingViewHolder = null;


    public JokesAdapter(Context context, List<Post> objects, boolean isUserProfile) {
        this.context = context;
        this.jokes = objects;
        this.isUserProfile = isUserProfile;
        setHasStableIds(true);
    }

    public class JokesViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner, PopupMenu.OnMenuItemClickListener {
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
            binding.postbar.optionsImageButton.setOnClickListener(view -> {
                if(isUserProfile) {
                    PopupMenu popup = new PopupMenu(context, binding.postbar.optionsImageButton, Gravity.BOTTOM,0,R.style.PopupMenu);
                    popup.getMenuInflater().inflate(R.menu.menu_post, popup.getMenu());
                    popup.setOnMenuItemClickListener(this);
                    MenuPopupHelper menuHelper;
                    Class[] argTypes;
                    try {
                        Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                        fMenuHelper.setAccessible(true);
                        menuHelper = (MenuPopupHelper) fMenuHelper.get(popup);
                        argTypes = new Class[] { boolean.class };
                        menuHelper.getClass().getDeclaredMethod("setForceShowIcon",
                                argTypes).invoke(menuHelper, true);
                    } catch (Exception e) {}
                    if (!Constants.UID.equals(joke.getUID())){
                        popup.getMenu().removeItem(R.id.action_delete);
                        popup.getMenu().removeItem(R.id.action_edit);
                    }
                    popup.show();
                }
            });

            binding.postbar.collectionImageButton.setOnClickListener(view -> showAddToCollectionDialog());

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

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    delete();
                    return true;
                case R.id.action_edit:
                    edit();
                    return true;
                case R.id.action_report:
                    report();
                    return true;
                default:
                    return false;
            }
        }

        private void delete(){
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
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                });
            }
        }

        private void edit() {
            Intent intent;
            if (joke.getType() == Constants.TEXT) {
                intent = new Intent(context, NewPostActivity2.class);
                intent.putExtra("posttype",3);
            } else {
                if ((Boolean) joke.getMetaData().get("visual")){
                    intent = new Intent(context, VisualMediaPostSubmissionActivity.class);
                } else {
                    intent = new Intent(context, AudioMediaPostSubmissionActivity.class);
                }
            }
            intent.putExtra("post", joke);
            context.startActivity(intent);
        }

        private void report(){
            String postId = joke.getPushId();
            String contentCreatorId = joke.getUID();
            Intent intent = new Intent(context, ReportActivity.class);
            intent.putExtra("pushId", postId);
            intent.putExtra("ccID", contentCreatorId);
            context.startActivity(intent);
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
            case Constants.IMAGE_GIF:
                binding.textLayout.textRootLayout.setVisibility(View.GONE);
                binding.imageLayout.imageRootLayout.setVisibility(View.GONE);
                binding.videoLayout.videoFramelayout.setVisibility(View.GONE);
                return new JokesViewHolder(binding);
            case Constants.VIDEO_AUDIO:
                binding.textLayout.textRootLayout.setVisibility(View.GONE);
                binding.imageLayout.imageRootLayout.setVisibility(View.GONE);
                binding.gifLayout.gifRootlayout.setVisibility(View.GONE);
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
            holder.binding.textLayout.jokeTitleTextView.setText(joke.getTitle());
            boolean isEmpty = (joke.getBody() != null) && joke.getBody().isEmpty();
            if (isEmpty) {
                holder.binding.textLayout.jokeBodyTextView.setText(joke.getSynopsis());
            }else {
                String more = "...++";
                SpannableString ss = new SpannableString(joke.getSynopsis() + more);
                ss.setSpan(new StyleSpan(Typeface.BOLD),joke.getSynopsis().length(),
                        joke.getSynopsis().length() + more.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.binding.textLayout.jokeBodyTextView.setText(ss);
                holder.binding.textLayout.jokeBodyTextView.setClickable(true);
                holder.binding.textLayout.jokeBodyTextView.setOnClickListener(view -> {
                    Intent intent = new Intent(context, PostActivity.class);
                    intent.putExtra(context.getString(R.string.title), joke.getTitle());
                    intent.putExtra(context.getString(R.string.body), joke.getBody());
                    context.startActivity(intent);
                });
            }
        } else if (joke.getType() == Constants.IMAGE_GIF) {
            Glide.with(context).load(joke.getMediaURL())
                    .into(holder.binding.gifLayout.postGifimageview);
        } else if (joke.getType() == Constants.VIDEO_AUDIO) {
            holder.setJoke(joke);
            holder.getLifecycle().addObserver(new VideoLifeCyclerObserver(context, holder, this));
            prepareVideoPlayback(holder);
            holder.orientationControlViewModel.getOrientationLiveData().observe(holder, orientationChanged -> {
                //currently doesn't get called
                if (orientationChanged && holder.equals(getNowPlayingViewHolder())){
                    //todo there is a lag after rotation, show loading spinner
                    holder.binding.videoLayout.postVideoView.getPlayer().setPlayWhenReady(false);
                    VideoInfo videoInfo = new VideoInfo(joke.getMediaURL(),
                            holder.binding.videoLayout.postVideoView.getPlayer().getCurrentPosition());
                    holder.orientationControlViewModel.getVideoLiveData().setValue(videoInfo);
                } else if (!orientationChanged && holder.equals(getNowPlayingViewHolder())){
                    holder.orientationControlViewModel.getVideoLiveData().observe(holder, videoInfo -> {
                        holder.binding.videoLayout.postVideoView.getPlayer().seekTo(videoInfo.getTimeElapsed());
                        holder.binding.videoLayout.postVideoView.getPlayer().setPlayWhenReady(true);
                    });
                }
            });
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
            String number2 = num + "";
            holder.binding.postbar.likesCounterTextView.setText(number2);
        });

        viewHolderViewModel.getNumCommentsLiveData().observe(holder, num -> {
            String number3 = num + "";
            holder.binding.postbar.commentCounterTextView.setText(number3);
        });

        viewHolderViewModel.getIsLikedLiveData().observe(holder, aBoolean -> {
            if (aBoolean) {
                Glide.with(context.getApplicationContext()).load(R.drawable.hilarity_mask_like).into(holder.binding.postbar.favoriteImageButton);
            } else {
                Glide.with(context.getApplicationContext()).load(R.drawable.hilarity_mask_unlike).into(holder.binding.postbar.favoriteImageButton);
            }
            holder.setIsLiked(aBoolean);
        });

        holder.binding.postbar.timeDateTextView.setText(Constants.formattedTimeString(context, joke.getTimeStamp(), false));

        holder.binding.postbar.favoriteImageButton.setOnClickListener(view -> {
            final String path = "userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/likes/list/" + Constants.UID;
            if (holder.getIsLiked()) {
                Constants.DATABASE.child(path).removeValue((databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        Glide.with(context).load(R.drawable.hilarity_mask_unlike).into(holder.binding.postbar.favoriteImageButton);
                        Constants.DATABASE.child("userlikes/" + Constants.UID + "/list/" + joke.getUID() + " " + joke.getPushId()).removeValue();
                    }
                });
            } else {
                Constants.DATABASE.child(path).setValue(true, (databaseError, databaseReference) -> {
                    if (databaseError == null){
                        Glide.with(context).load(R.drawable.hilarity_mask_like).into(holder.binding.postbar.favoriteImageButton);
                        Constants.DATABASE.child("userlikes/" + Constants.UID + "/list/" + joke.getUID() + " " + joke.getPushId()).setValue(joke);
                    }
                });
            }
        });

        holder.binding.postbar.commentImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra(context.getString(R.string.uid), joke.getUID());
            intent.putExtra(context.getString(R.string.pushId), joke.getPushId());
            context.startActivity(intent);
            }
        );
    }

    @Override
    public void onViewAttachedToWindow(@NonNull JokesViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        if (holder.getJoke() != null && holder.getJoke().getType() == Constants.VIDEO_AUDIO) {
            prepareVideoPlayback(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull JokesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
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
            ((SimpleExoPlayer) holder.binding.videoLayout.postVideoView.getPlayer()).prepare(new ProgressiveMediaSource
                    .Factory(dataSourceFactory,extractorsFactory)
                    .createMediaSource(Uri.parse(holder.getJoke().getMediaURL())));
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

    public List<Post> getJokes() {
        return jokes;
    }
}
