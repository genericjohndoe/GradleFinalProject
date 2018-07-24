package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.SubsItemBinding;
import com.udacity.gradle.builditbigger.isFollowing.IsFollowingLiveData;

import java.util.List;

/**
 * class preps hilarityuser objects for recyclerview
 */

public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.SubsViewHolder> {

    private List<HilarityUser> subscribersList;
    private Context context;

    public SubsAdapter(List<HilarityUser> list, Context context) {
        subscribersList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SubsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.subs_item, parent, false);
        return new SubsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubsViewHolder holder, int position) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_START);
        Glide.with(context).load(subscribersList.get(position).getUrlString()).into(holder.binding.subsProfileImageview);
        holder.binding.usernameTextView.setText(subscribersList.get(position).getUserName());
        holder.setUid(subscribersList.get(position).getUid());
        new IsFollowingLiveData(holder.getUid()).observe(holder, bool ->{
            holder.setIsFollowed(bool);
            if (bool){
                holder.binding.followButton.setText(context.getString(R.string.subscribed));
            } else {
                holder.binding.followButton.setText(context.getString(R.string.subscribe));
            }
        });
        //holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return subscribersList.size();
    }

    public class SubsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, LifecycleOwner {
        private String uid;
        public SubsItemBinding binding;
        private boolean isFollowed = false;
        private LifecycleRegistry mLifecycleRegistry;

        public SubsViewHolder(SubsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            mLifecycleRegistry = new LifecycleRegistry(this);
            mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
            binding.followButton.setOnClickListener(view2 -> {
                if (!isFollowed) {
                    Constants.DATABASE.child("followers/" + getUid() + "/list/" + Constants.UID).setValue(Constants.USER);

                } else {
                    Constants.DATABASE.child("followers/" + getUid() + "/list/" + Constants.UID).removeValue();
                }
            });
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, HilarityActivity.class);
            intent.putExtra("number", 4);
            intent.putExtra("uid", uid);
            context.startActivity(intent);
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        public void setUid(String uid){
            this.uid = uid;
        }

        public String getUid(){return uid;}

        public void setIsFollowed(boolean isFollowed){this.isFollowed = isFollowed;}

        public LifecycleRegistry getmLifecycleRegistry() {
            return mLifecycleRegistry;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SubsViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SubsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onViewRecycled(@NonNull SubsViewHolder holder) {
        holder.getmLifecycleRegistry().handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onViewRecycled(holder);
    }

    public void setSubscribersList(List<HilarityUser> list){
        subscribersList = list;
        notifyDataSetChanged();
    }
}
