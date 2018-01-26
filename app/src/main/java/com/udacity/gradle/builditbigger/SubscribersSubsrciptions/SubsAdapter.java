package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.FloatRange;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.SubsItemBinding;
import com.udacity.gradle.builditbigger.isFollowing.IsFollowingViewHolder;
import com.udacity.gradle.builditbigger.isFollowing.IsFollowingViewModelFactory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joeljohnson on 11/27/17.
 */

public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.SubsViewHolder> {

    List<HilarityUser> subscribersList;
    Context context;

    public SubsAdapter(List<HilarityUser> list, Context context) {
        subscribersList = list;
        this.context = context;
    }

    @Override
    public SubsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SubsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.subs_item, parent, false);
        return new SubsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final SubsViewHolder holder, int position) {
        Glide.with(context).load(subscribersList.get(position).getUrlString()).into(holder.binding.subsProfileImageview);
        holder.binding.usernameTextView.setText(subscribersList.get(position).getUserName());
        holder.setUid(subscribersList.get(position).getUID());
        Constants.DATABASE.child("followers/" + holder.getUid() + "/list/"+ Constants.UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            holder.setIsFollowed(true);
                            //todo follow button state set to "followed"
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return subscribersList.size();
    }

    public class SubsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String uid;
        public SubsItemBinding binding;
        private boolean isFollowed = false;

        public SubsViewHolder(SubsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.followButton.setOnClickListener(view2 -> {
                if (!isFollowed) {
                    Constants.DATABASE.child("followers/" + getUid() + "/list/" + Constants.UID).setValue(Constants.USER);
                    //todo set UI
                } else {
                    Constants.DATABASE.child("followers/" + getUid() + "/list/" + Constants.UID).removeValue();
                    //todo set UI
                }
            });
        }

        @Override
        public void onClick(View v) {
            Constants.changeFragment(R.id.hilarity_content_frame, Profile.newInstance(uid));
        }

        public void setUid(String uid){
            this.uid = uid;
        }

        public String getUid(){return uid;}

        public void setIsFollowed(boolean isFollowed){this.isFollowed = isFollowed;}
    }

}
