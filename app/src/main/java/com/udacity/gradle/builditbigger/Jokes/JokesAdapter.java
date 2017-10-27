package com.udacity.gradle.builditbigger.Jokes;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.Joke.JokeActivity;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

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
        TextView jokeName;
        TextView userName;
        TextView jokeBody;
        ImageButton likeButton;

        public JokesViewHolder(View view) {
            super(view);
            jokeName = (TextView) view.findViewById(R.id.joke_name);
            userName = (TextView) view.findViewById(R.id.user_name);
            jokeBody = (TextView) view.findViewById(R.id.joke_body);
            likeButton = (ImageButton) view.findViewById(R.id.like_button);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO change state of button, check it posted is liked first
                    //TODO change info in database
                }
            });
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {
            TextView jokeName = (TextView) view.findViewById(R.id.joke_name);
            TextView userName = (TextView) view.findViewById(R.id.user_name);
            TextView jokeBody = (TextView) view.findViewById(R.id.joke_body);
            Intent intent = new Intent(context, JokeActivity.class);
            intent.putExtra(context.getString(R.string.jokeBody), jokeBody.getText().toString());
            intent.putExtra(context.getString(R.string.userName), userName.getText().toString());
            intent.putExtra(context.getString(R.string.jokeTitle), jokeName.getText().toString());
            context.startActivity(intent);
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
    public void onBindViewHolder(JokesViewHolder holder, int position) {
        Joke joke = jokes.get(position);
        holder.jokeName.setText(joke.getJokeTitle());
        holder.userName.setText(joke.getUserName());
        holder.jokeBody.setText(joke.getJokeBody());
    }
}
