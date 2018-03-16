package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Database.HilarityUserDatabase;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Profile.Profile;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.MessagedUserCellBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * MessagedUsersAdapter class generates user icons to indicate messaged users
 */

public class MessagedUsersAdapter extends RecyclerView.Adapter<MessagedUsersAdapter.MessagedUserViewHolder> {
    //todo don't draw cell for "yourself"
    //todo inc user name
    //todo use room database
    private Context context;
    private List<HilarityUser> hilarityUsers;

    public MessagedUsersAdapter(String[] users, Context context){
        this.context = context;
        hilarityUsers = new ArrayList<>();
        for (String user: users){
            Constants.DATABASE.child("users/"+user).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hilarityUsers.add(dataSnapshot.getValue(HilarityUser.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        //hilarityUsers = HilarityUserDatabase.getInstance(context).dao().getUsersFromUidList(users);
    }

    @Override
    public void onBindViewHolder(MessagedUserViewHolder holder, int position) {
        Glide.with(context).load(hilarityUsers.get(position).getUrlString()).into(holder.bind.profileImageview);
        holder.uid = hilarityUsers.get(position).getUID();
    }

    @Override
    public MessagedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MessagedUserCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.messaged_user_cell, parent, false);
        return new MessagedUserViewHolder(bind);
    }

    @Override
    public int getItemCount() {
        return hilarityUsers.size();
    }

    /**
     * MessagedUserViewHolder class used to generate messaged user cell
     */
    public class MessagedUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MessagedUserCellBinding bind;
        private String uid;

        public MessagedUserViewHolder(MessagedUserCellBinding bind){
             super(bind.getRoot());
             this.bind = bind;
             bind.getRoot().setOnClickListener(this);
         }

        @Override
        public void onClick(View v) {
            Constants.changeFragment(R.id.hilarity_content_frame, Profile.newInstance(uid));
        }
    }
}
