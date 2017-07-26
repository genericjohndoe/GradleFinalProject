package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joeljohnson on 7/26/17.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    Context context;
    List<String> genres;
    RecyclerViewCallback rvc;


    public GenreAdapter(Context context, List<String> genres, RecyclerViewCallback rvc) {
        this.context = context;
        this.genres = genres;
        this.rvc = rvc;
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView genre;

        public GenreViewHolder(View view) {
            super(view);
            genre = (TextView) view.findViewById(R.id.languages);
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            Intent intent = new Intent(context, JokesActivity.class);
            intent.putExtra(context.getString(R.string.genres), tv.getText().toString());
            intent.putExtra(context.getString(R.string.languages), rvc.passItem());
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    @Override
    public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.languages, null);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GenreViewHolder holder, int position) {
        String language = genres.get(position);
        holder.genre.setText(language);
    }
}
