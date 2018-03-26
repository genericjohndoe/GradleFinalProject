package com.udacity.gradle.builditbigger.Messaging.ComposeMessage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.Interfaces.CreateChip;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.HilarityUserNetworkBinding;

import java.util.List;

/**
 * Created by joeljohnson on 1/13/18.
 */

public class UsersToMessageAdapter extends RecyclerView.Adapter<UsersToMessageAdapter.HilarityUserViewHolder> {

    private List<HilarityUser> hilarityUserList;
    private CreateChip cc;
    private Context context;

    UsersToMessageAdapter(List<HilarityUser> hilarityUserList, CreateChip cc, Context context){
        this.hilarityUserList = hilarityUserList;
        this.cc = cc;
        this.context = context;
    }

    @Override
    public HilarityUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HilarityUserNetworkBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.hilarity_user_network, parent, false);
        return new HilarityUserViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(HilarityUserViewHolder holder, int position) {
        HilarityUser hu = hilarityUserList.get(position);
        Log.i("Hilarity","hu is null? "+ Boolean.toString(hu == (null)));
        holder.hu = hu;
        Glide.with(context).load(hu.getUrlString()).into(holder.bind.profileImageview);
        holder.bind.usernameTextView.setText(hu.getUserName());
        Log.i("Hilarity", "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return hilarityUserList.size();
    }

    public void setHilarityUserList(List<HilarityUser> list){
        this.hilarityUserList = list;
        notifyDataSetChanged();
        Log.i("Hilarity","setHilarityUserList");
    }

    public class HilarityUserViewHolder extends RecyclerView.ViewHolder {
        HilarityUserNetworkBinding bind;
        HilarityUser hu;

        public HilarityUserViewHolder(HilarityUserNetworkBinding bind) {
            super(bind.getRoot());
            Log.i("Hilarity", "HVH created");
            this.bind = bind;
            this.bind.isPickedCheckBox.setOnClickListener(view -> {
                Log.i("Hilarity","hu is null in onClick? "+ Boolean.toString(hu == (null)));
                cc.addChipView(hu);
            });
            if (hu !=null && cc.getSelectedUsers().contains(hu)){
                bind.isPickedCheckBox.setChecked(true);
            }
        }
    }

}
