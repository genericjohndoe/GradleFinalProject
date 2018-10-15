package com.udacity.gradle.builditbigger.messaging.transcripts;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.MessagedUserCellBinding;

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

    public MessagedUsersAdapter(List<HilarityUser> hilarityUsers, Context context){
        this.context = context;
        this.hilarityUsers = hilarityUsers;
        //hilarityUsers = HilarityUserDatabase.getInstance(context).dao().getUsersFromUidList(users);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagedUserViewHolder holder, int position) {
        Glide.with(context).load(hilarityUsers.get(position).getUrlString()).into(holder.bind.profileImageview);
        Log.i("HilarityMessage12", "position "+position);
        holder.uid = hilarityUsers.get(position).getUid();
    }

    @NonNull
    @Override
    public MessagedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessagedUserCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.messaged_user_cell, parent, false);
        Log.i("HilarityMessage12", "view holder made");
        return new MessagedUserViewHolder(bind);
    }

    @Override
    public int getItemCount() {return hilarityUsers.size();}

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
            Intent intent = new Intent(context, HilarityActivity.class);
            intent.putExtra("number", 4);
            intent.putExtra("uid", uid);
            context.startActivity(intent);
        }
    }
}
