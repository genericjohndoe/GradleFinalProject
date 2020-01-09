package com.udacity.gradle.builditbigger.language;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.interfaces.RecyclerViewCallback;

/**
 * DEPRECATED
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private Context context;
    private RecyclerViewCallback rvc;
    private String[] languages = new String[]{"English", "Español", "Français", "Deutsch", "Português", "русский",
            "العربية", "中文", "日本語", "हिंदी"};


    public LanguageAdapter(Context context, RecyclerViewCallback rvc) {
        this.context = context;
        this.rvc = rvc;
    }

    public class LanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView language;


        public LanguageViewHolder(View view) {
            super(view);
            language =  view.findViewById(R.id.languages);
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

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.languages, null);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        String language = languages[position];
        holder.language.setText(language);
    }
}
