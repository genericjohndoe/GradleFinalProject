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
 * Created by joeljohnson on 7/25/17.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    Context context;
    List<String> languages;


    public LanguageAdapter(Context context, List<String> languages) {
        this.context = context;
        this.languages = languages;
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView language;

        public LanguageViewHolder(View view) {
            super(view);
            language = (TextView) view.findViewById(R.id.languages);
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {
            TextView tv = (TextView) view;
            String text = tv.getText().toString();
            Intent intent = new Intent(context, GenreActivity.class);
            intent.putExtra(context.getString(R.string.languages), text + " Genres");
            context.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.languages, null);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        String language = languages.get(position);
        holder.language.setText(language);
    }
}
