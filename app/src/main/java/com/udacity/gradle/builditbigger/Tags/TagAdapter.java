package com.udacity.gradle.builditbigger.Tags;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.TagCellBinding;

import java.util.List;

/**
 * Created by joeljohnson on 3/18/18.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {
    List<String> tags;
    Context context;

    public TagAdapter(List<String> tags, Context context){
        this.tags = tags;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.bind.tag.setText(tag);
        holder.tag = tag;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TagCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.tag_cell, parent, false);
        return new TagViewHolder(bind);
    }

    public class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TagCellBinding bind;
        String tag;

        public TagViewHolder(TagCellBinding bind){
            super(bind.getRoot());
            this.bind = bind;
            bind.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Constants.changeFragment(R.id.hilarity_content_frame, TaggedJokesFragment.newInstance(tag), (AppCompatActivity) context);
        }
    }

    public void setTags(List<String> tags){
        this.tags = tags;
        notifyDataSetChanged();
    }
}
