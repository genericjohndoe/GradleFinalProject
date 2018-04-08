package com.udacity.gradle.builditbigger.Collections;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Jokes.JokesActivity;
import com.udacity.gradle.builditbigger.Models.Collection;

import com.udacity.gradle.builditbigger.R;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * CLASS FOR STYLING GENRE for recyclerview
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    Context context;
    List<Collection> genres;


    public CollectionAdapter(Context context, List<Collection> genres) {
        this.context = context;
        this.genres = genres;
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView genreTitle;
        ImageButton lockButton;
        Collection collection;
        Button subButton;

        public CollectionViewHolder(View view) {
            super(view);
            genreTitle = view.findViewById(R.id.genre_title_textview);
            lockButton = view.findViewById(R.id.lock_imageButton);
            lockButton.setOnClickListener(view2-> {
                    //todo switch between lock and unlock if genre was created by user
                    //todo after checking if the changes in the database are made
            });
            subButton = view.findViewById(R.id.subscribe_button);
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, JokesActivity.class);
            intent.putExtra("collectionId", collection.getGenreId());
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    @Override @NonNull
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_text_view, null);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection genre = genres.get(position);
        holder.genreTitle.setText(genre.getTitle());
        holder.collection = genre;
        if (genre.getUID().equals(Constants.UID)){
            holder.subButton.setVisibility(View.GONE);
            if (genre.getRestricted()){
                holder.lockButton.setBackgroundResource(R.drawable.ic_lock_outline_black_24dp);
            } else {
                holder.lockButton.setBackgroundResource(R.drawable.ic_lock_open_black_24dp);
            }
        } else {
            holder.lockButton.setVisibility(View.GONE);
            holder.subButton.setOnClickListener(view ->{/*subscribe*/});
        }
    }

    public void setGenres(List<Collection> genres) {
        this.genres = genres;
        notifyDataSetChanged();
    }
}
