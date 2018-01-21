package com.udacity.gradle.builditbigger.Messaging;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.CreateChip;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by joeljohnson on 1/13/18.
 */

public class UsersToMessageAdapter extends RecyclerView.Adapter<UsersToMessageAdapter.HilarityUserViewHolder> {

    List<HilarityUser> hilarityUserList;
    CreateChip cc;
    Context context;

    UsersToMessageAdapter(List<HilarityUser> hilarityUserList, CreateChip cc, Context context){
        this.hilarityUserList = hilarityUserList;
        this.cc = cc;
        this.context = context;
    }

    @Override
    public HilarityUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_post, parent, false);
        return new HilarityUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HilarityUserViewHolder holder, int position) {
        HilarityUser hu = hilarityUserList.get(position);
        Glide.with(context).load(hu.getUrlString()).into(holder.profileImg);
        holder.userName.setText(hu.getUserName());
    }

    @Override
    public int getItemCount() {
        return hilarityUserList.size();
    }

    public class HilarityUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView profileImg;
        TextView userName;
        CheckBox isPicked;

        public HilarityUserViewHolder(View view){
            super(view);
            profileImg = view.findViewById(R.id.profile_imageview);
            userName = view.findViewById(R.id.username_textView);
            isPicked = view.findViewById(R.id.is_picked_checkBox);
        }

        @Override
        public void onClick(View view) {
            isPicked.setChecked(true);
            cc.addChipView(hilarityUserList.get(getAdapterPosition()));
        }
    }
}
