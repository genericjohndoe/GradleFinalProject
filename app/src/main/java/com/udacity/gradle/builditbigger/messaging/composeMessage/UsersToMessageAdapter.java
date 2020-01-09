package com.udacity.gradle.builditbigger.messaging.composeMessage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.HilarityUserNetworkBinding;
import com.udacity.gradle.builditbigger.interfaces.CreateChip;
import com.udacity.gradle.builditbigger.models.HilarityUser;

import java.util.List;

/**
 * Created by joeljohnson on 1/13/18.
 */

public class UsersToMessageAdapter extends RecyclerView.Adapter<UsersToMessageAdapter.HilarityUserViewHolder> {

    private List<HilarityUser> hilarityUserList;
    private CreateChip cc;
    private Context context;

    UsersToMessageAdapter(List<HilarityUser> hilarityUserList, CreateChip cc, Context context) {
        this.hilarityUserList = hilarityUserList;
        this.cc = cc;
        this.context = context;
    }

    @Override
    @NonNull
    public HilarityUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HilarityUserNetworkBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.hilarity_user_network, parent, false);
        return new HilarityUserViewHolder(bind);
    }

    @Override
    public void onBindViewHolder(@NonNull HilarityUserViewHolder holder, int position) {
        HilarityUser hu = hilarityUserList.get(position);
        holder.hu = hu;
        Glide.with(context).load(hu.getUrlString()).into(holder.bind.profileImageview);
        holder.bind.usernameTextView.setText(hu.getUserName());
    }

    @Override
    public int getItemCount() {
        return hilarityUserList.size();
    }


    public void setHilarityUserList(List<HilarityUser> list) {
        this.hilarityUserList = list;
        notifyDataSetChanged();
    }

    public class HilarityUserViewHolder extends RecyclerView.ViewHolder {
        HilarityUserNetworkBinding bind;
        HilarityUser hu;

        public HilarityUserViewHolder(HilarityUserNetworkBinding bind) {
            super(bind.getRoot());
            Log.i("Hilarity", "HVH created");
            this.bind = bind;
            this.bind.isPickedCheckBox.setOnClickListener(view ->{
                if (bind.isPickedCheckBox.isChecked()){
                    cc.addChipView(hu);
                }else {
                    cc.removeChipView(hu);
                }
            });
            if (hu != null && cc.getSelectedUsers().contains(hu)) {
                bind.isPickedCheckBox.setChecked(true);
            }
        }
    }
}
