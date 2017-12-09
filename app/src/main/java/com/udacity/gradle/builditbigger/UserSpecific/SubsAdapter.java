package com.udacity.gradle.builditbigger.UserSpecific;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joeljohnson on 11/27/17.
 */

public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.ViewHolder> {

    List<String> subscribersList;
    Fragment fragment;

    public SubsAdapter(List<String> list, Fragment fragment) {
        subscribersList = list;
        this.fragment = fragment;
    }

    @Override
    public SubsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subs_item, parent, false);
        return new SubsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FirebaseDatabase.getInstance().getReference("users/" + subscribersList.get(position))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HilarityUser user = dataSnapshot.getValue(HilarityUser.class);
                        Glide.with(fragment).load(user.getUrlString()).into(holder.profileImage);
                        holder.userName.setText(user.getUserName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return subscribersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileImage;
        public TextView userName;
        public Button followButton;

        public ViewHolder(View view) {
            super(view);
            profileImage = view.findViewById(R.id.subs_profile_imageview);
            userName = view.findViewById(R.id.username_textView);
            followButton = view.findViewById(R.id.follow_button);
            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo set animation after button is pressed
                    //todo modify database
                }
            });
        }

    }
}
