package com.udacity.gradle.builditbigger.Collections;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Jokes.JokesActivity;
import com.udacity.gradle.builditbigger.Models.Collection;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.GenreTextViewBinding;

import java.util.List;

/**
 * CLASS FOR STYLING collection for recyclerview
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private Context context;
    private List<Collection> collections;


    public CollectionAdapter(Context context, List<Collection> collections) {
        this.context = context;
        this.collections = collections;
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Collection collection;
        GenreTextViewBinding bind;

        public CollectionViewHolder(GenreTextViewBinding bind) {
            super(bind.getRoot());
            this.bind = bind;

            bind.lockImageButton.setOnClickListener(view2-> {
                    Constants.DATABASE
                            .child("usercollections/"+collection.getUID()+"/"+collection.getGenreId()+"/restricted")
                            .setValue(!collection.getRestricted(), (databaseError, databaseReference) -> {
                                if (databaseError == null){
                                    collection.setRestricted(!collection.getRestricted());
                                    if (collection.getRestricted()){
                                        bind.lockImageButton.setImageResource(R.drawable.ic_lock_outline_black_24dp);
                                    } else {
                                        bind.lockImageButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
                                    }
                                }
                            });
            });
            bind.getRoot().setOnClickListener(this);
            bind.getRoot().setTag(this);
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
        return collections.size();
    }

    @Override @NonNull
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GenreTextViewBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.genre_text_view, parent, false);
        return new CollectionViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection genre = collections.get(position);
        holder.bind.genreTitleTextview.setText(genre.getTitle());
        holder.collection = genre;
        if (genre.getUID().equals(Constants.UID)){
            holder.bind.subscribeButton.setVisibility(View.GONE);
            if (genre.getRestricted()){
                holder.bind.lockImageButton.setImageResource(R.drawable.ic_lock_outline_black_24dp);
            } else {
                holder.bind.lockImageButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
            }
        } else {
            holder.bind.lockImageButton.setVisibility(View.GONE);
            holder.bind.subscribeButton.setOnClickListener(view ->{/*subscribe*/});
        }
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public List<Collection> getCollections() {
        return collections;
    }
}
