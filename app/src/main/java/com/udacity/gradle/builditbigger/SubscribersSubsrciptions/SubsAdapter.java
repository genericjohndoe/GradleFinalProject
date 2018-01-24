package com.udacity.gradle.builditbigger.SubscribersSubsrciptions;

import android.content.Context;
import android.support.v4.app.Fragment;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subs_item, parent, false);
        return new SubsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubsViewHolder holder, int position) {
        Glide.with(context).load(subscribersList.get(position).getUrlString()).into(holder.profileImage);
        holder.userName.setText(subscribersList.get(position).getUserName());
        holder.setUid(subscribersList.get(position).getUID());
    }

    @Override
    public int getItemCount() {
        return subscribersList.size();
    }

    public class SubsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView profileImage;
        public TextView userName;
        public Button followButton;
        private String uid;

        public SubsViewHolder(View view) {
            super(view);
            profileImage = view.findViewById(R.id.subs_profile_imageview);
            userName = view.findViewById(R.id.username_textView);
            followButton = view.findViewById(R.id.follow_button);
            followButton.setOnClickListener(view2 ->{

            });
        }

        @Override
        public void onClick(View v) {
            Constants.changeFragment(R.id.hilarity_content_frame, Profile.newInstance(uid));
        }

        public void setUid(String uid){
            this.uid = uid;
        }

    }
}
