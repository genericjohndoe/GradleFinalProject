package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Models.Message;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentTranscriptBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * TranscriptFragment class shows messages exchanged between users
 */

public class TranscriptFragment extends Fragment {
    private String uid;
    private String path;
    private String[] usersUidList;
    private List<HilarityUser> hilarityUsers;

    public static TranscriptFragment newInstance(String uid, String path){
        TranscriptFragment transcriptFragment = new TranscriptFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid",uid);
        bundle.putString("path", path);
        transcriptFragment.setArguments(bundle);
        return transcriptFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hilarityUsers = new ArrayList<>();
        if (getArguments() != null){
            uid = getArguments().getString("uid");
            path = getArguments().getString("path");
            if (path != null) usersUidList = path.split(", ");
        }
        for (String uid: usersUidList){
            Constants.DATABASE.child("users/"+uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    hilarityUsers.add(dataSnapshot.getValue(HilarityUser.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentTranscriptBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_transcript,container,false);
        //shows horizontal list of users

        bind.usersRecyclerView.setAdapter(new MessagedUsersAdapter(hilarityUsers, getActivity()));
        bind.usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //shows list of messages
        List<Message> messages = new ArrayList<>();
        MessagesAdapter messagesAdapter = new MessagesAdapter(messages, getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false);
        llm.setStackFromEnd(true);
        bind.messagesRecyclerView.setAdapter(messagesAdapter);
        bind.messagesRecyclerView.setLayoutManager(llm);
        MessagesViewModel messagesViewModel = ViewModelProviders.of(this,new MessagesViewModelFactory(uid,path)).get(MessagesViewModel.class);
        messagesViewModel.getMessagesLiveData().observe(this, message -> {
            if (!messages.contains(message)) {
                messages.add(message);
                messagesAdapter.notifyDataSetChanged();
                bind.messagesRecyclerView.scrollToPosition(messages.size()-1);
            }
        });
        bind.sendButton.setOnClickListener(view ->{
            String text = bind.messageEditText.getText().toString();
            DatabaseReference db = Constants.DATABASE.child("messages/"+uid+"/"+path+"/messagelist").push();
            Message message = new Message(Constants.USER,text,System.currentTimeMillis(), db.getKey(), true);
            db.setValue(message, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    Constants.DATABASE.child("transcriptpreviews/"+uid+"/"+path+"/message")
                            .setValue(message);
                }
            });
            bind.messageEditText.setText("");
            InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(bind.messageEditText.getWindowToken(), 0);
        });

        return bind.getRoot();
    }
}
