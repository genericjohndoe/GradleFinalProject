package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joeljohnson on 7/17/17.
 */

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.JokeViewHolder> {
    Context context;
    List<Joke> jokes;


    public JokeAdapter(Context context, List<Joke> objects) {
        this.context = context;
        this.jokes = objects;


    }

    public class JokeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView jokeName;
        TextView userName;
        TextView jokeBody;

        public JokeViewHolder(View view) {
            super(view);
            jokeName = (TextView) view.findViewById(R.id.joke_name);
            userName = (TextView) view.findViewById(R.id.user_name);
            jokeBody = (TextView) view.findViewById(R.id.joke_body);
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {}
    }


    @Override
    public int getItemCount() {
        return jokes.size();
    }

    @Override
    public JokeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, null);
        return new JokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JokeViewHolder holder, int position) {
        Joke joke = jokes.get(position);
        holder.jokeName.setText(joke.getJokeTitle());
        holder.userName.setText(joke.getUserName());
        holder.jokeBody.setText(joke.getJokeBody());
    }
}
