package com.udacity.gradle.builditbigger.Jokes;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.widget.SocialTextView;
import com.udacity.gradle.builditbigger.Comments.CommentActivity;
import com.udacity.gradle.builditbigger.Comments.CommentFragment;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.Joke;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.databinding.GenericPostBinding;

import java.util.List;

/**
 * Class styles posts for recyclerview
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.JokesViewHolder> {
    Context context;
    List<Joke> jokes;
    boolean isUserProfile;

    public JokesAdapter(Context context, List<Joke> objects, boolean isUserProfile) {
        this.context = context;
        this.jokes = objects;
        this.isUserProfile = isUserProfile;
        setHasStableIds(true);
    }

    public class JokesViewHolder extends RecyclerView.ViewHolder implements LifecycleOwner {
        GenericPostBinding binding;

        private LifecycleRegistry mLifecycleRegistry;
        private boolean isLiked = false;
        private Joke joke;

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
                        //todo send to search page
                        Log.i("HilarityTag", s);
                        return null;
                    }
            );
            binding.optionsImageButton.setOnClickListener(view ->{
                PopupMenu popup = new PopupMenu(context, binding.optionsImageButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_post, popup.getMenu());

                popup.setOnMenuItemClickListener(item ->{
                        if (joke != null){
                            Constants.DATABASE.child("userposts/"+joke.getUID()+"/posts/"+joke.getPushId()).removeValue((databaseError, databaseReference) -> {
                                jokes.remove(joke);
                                notifyDataSetChanged();
                            });
                        }
                        return true;
                });

                popup.show();

            });
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

        public void setJoke(Joke joke) {
            this.joke = joke;
        }

        public Joke getJoke() {
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
        return null;
    }

    @Override
    public void onBindViewHolder(final JokesViewHolder holder, int position) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
        final Joke joke = jokes.get(position);
        holder.joke = joke;

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
                Glide.with(context).load(R.drawable.ic_favorite_black_24dp).into(holder.binding.favoriteImageButton);
            } else {
                Glide.with(context).load(R.drawable.ic_favorite_border_black_24dp).into(holder.binding.favoriteImageButton);
            }
            holder.setIsLiked(aBoolean);
        });

        holder.binding.timeDateTextView.setText(Constants.formattedTimeString(context, joke.getTimeStamp()));

        holder.binding.favoriteImageButton.setOnClickListener(view -> {
            final String path = "userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/likes/list/" + Constants.UID;
            if (holder.getIsLiked()) {
                Constants.DATABASE.child(path).removeValue();
                Glide.with(context).load(R.drawable.ic_favorite_border_black_24dp).into(holder.binding.favoriteImageButton);
            } else {
                Constants.DATABASE.child(path).setValue(true);
                Glide.with(context).load(R.drawable.ic_favorite_black_24dp).into(holder.binding.favoriteImageButton);
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
    public void onViewAttachedToWindow(JokesViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        if (holder.getJoke() != null && holder.getJoke().getType() == Constants.VIDEO) {
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
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (holder.binding.videoLayout.postVideoView.getPlayer() != null) {
            holder.binding.videoLayout.postVideoView.getPlayer().prepare(new ExtractorMediaSource(Uri.parse(holder.getJoke().getMediaURL()),
                    dataSourceFactory, extractorsFactory, null, null), false, false);
        }
    }

    public void setJokes(List<Joke> jokes) {
        this.jokes = jokes;
        notifyDataSetChanged();
    }
}
