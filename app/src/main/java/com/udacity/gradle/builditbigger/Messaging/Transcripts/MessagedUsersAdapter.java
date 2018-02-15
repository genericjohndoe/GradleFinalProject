package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Database.HilarityUserDatabase;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.MessagedUserCellBinding;

/**
 * Created by joeljohnson on 2/14/18.
 */

public class MessagedUsersAdapter extends RecyclerView.Adapter<MessagedUsersAdapter.MessagedUserViewHolder> {
    //todo don't draw cell for "yourself"
    private Context context;
    private HilarityUser[] hilarityUsers;

    public MessagedUsersAdapter(String[] users, Context context){
        this.context = context;
        hilarityUsers = HilarityUserDatabase.getInstance(context).dao().getUsersFromUidList(users);
    }

    @Override
    public void onBindViewHolder(MessagedUserViewHolder holder, int position) {
        Glide.with(context).load(hilarityUsers[position].getUrlString()).into(holder.bind.profileImageview);
        holder.uid = hilarityUsers[position].getUID();
    }

    @Override
    public MessagedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessagedUserCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.messaged_user_cell, parent, false);
        return new MessagedUserViewHolder(bind);
    }

    @Override
    public int getItemCount() {
        return hilarityUsers.length;
    }

    public class MessagedUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MessagedUserCellBinding bind;
        private String uid;
        public MessagedUserViewHolder(MessagedUserCellBinding bind){
             super(bind.getRoot());
             this.bind = bind;
         }

        @Override
        public void onClick(View v) {
            Constants.changeFragment(R.id.hilarity_content_frame, Profile.newInstance(uid));
        }
    }
}
