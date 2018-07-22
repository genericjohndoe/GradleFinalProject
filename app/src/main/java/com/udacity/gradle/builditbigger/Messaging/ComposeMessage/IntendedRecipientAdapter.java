package com.udacity.gradle.builditbigger.Messaging.ComposeMessage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.udacity.gradle.builditbigger.Interfaces.FilterRecyclerView;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.MessagedUserCellBinding;

import java.util.List;

public class IntendedRecipientAdapter extends RecyclerView.Adapter {

    private List<HilarityUser> hilarityUsers;
    private Context context;
    private static final int TYPE_EDIT_TEXT = 0;
    private static final int TYPE_ITEM = 1;
    private FilterRecyclerView filterRecyclerView;

    public IntendedRecipientAdapter(List<HilarityUser> hilarityUsers, Context context, FilterRecyclerView filterRecyclerView){
        this.hilarityUsers = hilarityUsers;
        this.context = context;
        this.filterRecyclerView = filterRecyclerView;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                MessagedUserCellBinding bind = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.messaged_user_cell, parent, false);
                return new IntendedRecipientViewHolder(bind);
            case TYPE_EDIT_TEXT:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edittext_view_holder, null);
                return new EditTextViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (hilarityUsers.size() > 0 && position != hilarityUsers.size()) {
            Log.i("wehwfueh", ""+position);
            HilarityUser hu = hilarityUsers.get(position);
            if (holder instanceof IntendedRecipientViewHolder) {
                ((IntendedRecipientViewHolder) holder).hu = hu;
                Glide.with(context).load(hu.getUrlString())
                        .into(((IntendedRecipientViewHolder) holder).bind.profileImageview);
            }
        }

    }

    @Override
    public int getItemCount() {return hilarityUsers.size() + 1;}

    @Override
    public int getItemViewType(int position) {
        if (position == hilarityUsers.size()) return TYPE_EDIT_TEXT;
        return TYPE_ITEM;
    }

    public class IntendedRecipientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MessagedUserCellBinding bind;
        HilarityUser hu;

        public IntendedRecipientViewHolder(MessagedUserCellBinding bind){
            super(bind.getRoot());
            this.bind = bind;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            hilarityUsers.remove(hu);
            notifyDataSetChanged();
        }
    }

    public class EditTextViewHolder extends  RecyclerView.ViewHolder{
        EditText editText;

        EditTextViewHolder(View view){
            super(view);
            editText = view.findViewById(R.id.edittext_view_holder);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterRecyclerView.filter(""+s);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            editText.setOnFocusChangeListener((view2, hasFocus) -> {
                if (hasFocus) {
                    filterRecyclerView.requestFocus();
                }else{
                    InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
            });
            editText.requestFocus();
        }
    }
}
