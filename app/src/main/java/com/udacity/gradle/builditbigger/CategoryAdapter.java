package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by joeljohnson on 9/28/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    String[] catNames = new String[]{"Jokes", "Genres", "Media", "Likes"};
    Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button catButton;


        public CategoryViewHolder(View view) {
            super(view);
            catButton =  view.findViewById(R.id.category_button);
            view.setOnClickListener(this);
            view.setTag(this);
        }

        @Override
        public void onClick(View view) {
            //rvc.passItem(language.getText().toString());
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        String category = catNames[position];
        holder.catButton.setText(category);
    }
}
