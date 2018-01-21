package com.udacity.gradle.builditbigger.Genres;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.gradle.builditbigger.Jokes.JokesActivity;
import com.udacity.gradle.builditbigger.Models.Genre;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

/**
 * Created by joeljohnson on 7/26/17.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    Context context;
    List<Genre> genres;


    public GenreAdapter(Context context, List<Genre> genres) {
        this.context = context;
        this.genres = genres;
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView genreTitle;
        ImageButton lockButton;

        public GenreViewHolder(View view) {
            super(view);
            genreTitle = view.findViewById(R.id.genre_title_textview);
            lockButton = view.findViewById(R.id.lock_imageButton);
            lockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo switch between lock and unlock if genre was created by user
                    //todo after checking if the changes in the database are made
                }
            });
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            Intent intent = new Intent(context, JokesActivity.class);
            intent.putExtra(context.getString(R.string.genres), tv.getText().toString());
            //intent.putExtra(context.getString(R.string.languages), rvc.passItem());
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_text_view, null);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        Genre genre = genres.get(position);
        holder.genreTitle.setText(genre.getTitle());
        //todo check if genre is restricted, set appropriate image
        Log.i("genre adapter", genre + " at " + position);
    }
}
