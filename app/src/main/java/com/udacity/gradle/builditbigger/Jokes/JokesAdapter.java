package com.udacity.gradle.builditbigger.Jokes;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.CommentFragment;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joeljohnson on 7/17/17.
 */

public class JokesAdapter extends RecyclerView.Adapter<JokesAdapter.JokesViewHolder> {
    Context context;
    List<Joke> jokes;
    int id = 1;


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
        TextView tagline;

        public JokesViewHolder(View view) {
            super(view);
            tagline = view.findViewById(R.id.tagline_textView);
            timeStampTextView = view.findViewById(R.id.time_date_textView);
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
    public int getItemViewType(int position) {
        return jokes.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    @Override
    public JokesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch  (viewType){
            case Constants.TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_post, parent, false);
                return new TextPostViewHolder(view);
             case Constants.IMAGE:
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_post, parent, false);
                 return new ImagePostViewHolder(view);
             case Constants.VIDEO:
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_post, parent, false);
                 return new VideoPostViewHolder(view);
        }
       return null;
    }

    @Override
    public void onBindViewHolder(final JokesViewHolder holder, int position) {
        final Joke joke = jokes.get(position);

        if (holder instanceof TextPostViewHolder) {
            ((TextPostViewHolder) holder).title.setText(joke.getJokeTitle());
            ((TextPostViewHolder) holder).body.setText(joke.getJokeBody());
            ((TextPostViewHolder) holder).tagline.setText(joke.getTagline());
        } else if (holder instanceof ImagePostViewHolder) {
            Glide.with(context).load(joke.getMediaURL()).into(((ImagePostViewHolder) holder).post);
            ((ImagePostViewHolder) holder).tagline.setText(joke.getTagline());
        } else {
            ((VideoPostViewHolder) holder).post.setVideoURI(Uri.parse(joke.getMediaURL()));
        }

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
                        holder.likeCounterTextView.setText(Integer.toString(0));
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
                        holder.commentCounterTextView.setText(Integer.toString(0));
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
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

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
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo start animation between fragments
                //todo show recyclerview of comments
                CommentFragment cf = new CommentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("uid", joke.getUID());
                bundle.putString("post id", joke.getPushId());
                cf.setArguments(bundle);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.hilarity_content_frame, cf)
                        .commit();
            }
        });
    }



    public class TextPostViewHolder extends JokesViewHolder{
        TextView title;
        TextView body;

        public TextPostViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.jokeTitle_textView);
            body = view.findViewById(R.id.jokeBody_textView);
        }

    }

    public class ImagePostViewHolder extends JokesViewHolder{
        ImageView post;

        public ImagePostViewHolder(View view){
            super(view);
            post = view.findViewById(R.id.post_imageview);
        }
    }

    public class VideoPostViewHolder extends JokesViewHolder{
        VideoView post;

        public VideoPostViewHolder(View view){
            super(view);
            post = view.findViewById(R.id.post_videoView);
            post.start();
        }
    }

}
