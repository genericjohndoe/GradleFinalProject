package com.udacity.gradle.builditbigger.Jokes;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.Posts.ImagePostContent;
import com.udacity.gradle.builditbigger.Posts.TextPostContent;
import com.udacity.gradle.builditbigger.Posts.VideoPostContent;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joeljohnson on 7/17/17.
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.JokesViewHolder> {
    Context context;
    List<Joke> jokes;


    public JokesAdapter(Context context, List<Joke> objects) {
        this.context = context;
        this.jokes = objects;
    }

    public class JokesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName;
        TextView likeCounterTextView;
        TextView commentCounterTextView;
        TextView timeStampTextView;
        CircleImageView profileImg;
        ImageButton likeButton;
        ImageButton options;
        ImageButton comments;
        FrameLayout frameLayout;


        public JokesViewHolder(View view) {
            super(view);
            timeStampTextView = view.findViewById(R.id.time_date_textView);
            frameLayout = view.findViewById(R.id.post_content);
            profileImg = view.findViewById(R.id.profile_imageview);
            userName = view.findViewById(R.id.username_textView);

            options = view.findViewById(R.id.options_imageButton);
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo open custom dialog
                }
            });

            likeButton = view.findViewById(R.id.favorite_imageButton);
            likeCounterTextView = view.findViewById(R.id.likes_counter_textView);

            comments = view.findViewById(R.id.comment_imageButton);
            commentCounterTextView = view.findViewById(R.id.comment_counter_textView);

            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {
            //todo open fragment to show all post data plus comments
        }
    }


    @Override
    public int getItemCount() {
        return jokes.size();
    }

    @Override
    public JokesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, null);
        return new JokesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final JokesViewHolder holder, int position) {
        final Joke joke = jokes.get(position);
        holder.userName.setText(joke.getUser());

        Constants.DATABASE.child("users/" + joke.getUID() + "/urlString")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String url = dataSnapshot.getValue(String.class);
                        Glide.with(context).load(url).into(holder.profileImg);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Constants.DATABASE.child("userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/likes/num")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer num = dataSnapshot.getValue(Integer.class);
                        holder.likeCounterTextView.setText(Integer.toString(num));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Constants.DATABASE.child("userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/comments/num")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Integer num = dataSnapshot.getValue(Integer.class);
                        holder.commentCounterTextView.setText(Integer.toString(num));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Constants.DATABASE.child("userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/likes/list/" + Constants.UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            holder.likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        } else {
                            holder.likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

        if (joke.getMediaURL().equals("")) {
            Bundle bundle = new Bundle();
            bundle.putString("jokeTitle", joke.getJokeTitle());
            bundle.putString("jokeBody", joke.getJokeBody());
            bundle.putString("tagline", joke.getTagline());
            //may not work
            TextPostContent tpc = new TextPostContent();
            tpc.setArguments(bundle);
            holder.frameLayout.addView(tpc.getView());
        } else if (joke.getMediaURL().contains(".png")) {//may need to take into account more file types
            Bundle bundle = new Bundle();
            bundle.putString("media_url", joke.getMediaURL());
            bundle.putString("tagline", joke.getTagline());
            ImagePostContent ipc = new ImagePostContent();
            ipc.setArguments(bundle);
            holder.frameLayout.addView(ipc.getView());
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("media_url", joke.getMediaURL());
            bundle.putString("tagline", joke.getTagline());
            VideoPostContent vpc = new VideoPostContent();
            vpc.setArguments(bundle);
            holder.frameLayout.addView(vpc.getView());
        }

        holder.timeStampTextView.setText(joke.getTimeStamp());

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String path = "userpostslikescomments/" + joke.getUID() + "/" + joke.getPushId() + "/likes/list/" + Constants.UID;
                Constants.DATABASE.child(path)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Constants.DATABASE.child(path).removeValue();
                                    holder.likeButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                } else {
                                    Constants.DATABASE.child(path).setValue(true);
                                    holder.likeButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo start animation between fragments
                //todo show recyclerview of comments
            }
        });
    }


}
