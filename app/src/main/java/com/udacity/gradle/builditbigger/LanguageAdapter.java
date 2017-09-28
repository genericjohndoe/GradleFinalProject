package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by joeljohnson on 7/25/17.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    Context context;
    RecyclerViewCallback rvc;
    String[] languages = new String[]{"English", "Español", "Français", "Deutsch", "Português","русский",
            "العربية","中文","日本語", "हिंदी"};


    public LanguageAdapter(Context context, RecyclerViewCallback rvc) {
        this.context = context;
        this.rvc = rvc;
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
            rvc.passItem(language.getText().toString());
        }
    }

    @Override
    public int getItemCount() {
        return languages.length;
    }

    @Override
    public LanguageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.languages, null);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LanguageViewHolder holder, int position) {
        String language = languages[position];
        holder.language.setText(language);
    }
}
