package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Message;
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
        if (getArguments() != null){
            uid = getArguments().getString("uid");
            path = getArguments().getString("path");
            usersUidList = path.split(", ");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentTranscriptBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_transcript,container,false);
        getActivity().setTitle("Transcript");
        //shows horizontal list of users
        bind.usersRecyclerView.setAdapter(new MessagedUsersAdapter(usersUidList, getActivity()));
        bind.usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        List<Message> messages = new ArrayList<>();
        MessagesAdapter messagesAdapter = new MessagesAdapter(messages, getActivity());
        //shows list of messages
        bind.messagesRecyclerView.setAdapter(messagesAdapter);
        bind.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        MessagesViewModel messagesViewModel = ViewModelProviders.of(this,new MessagesViewModelFactory(uid,path)).get(MessagesViewModel.class);
        messagesViewModel.getMessagesLiveData().observe(this, message -> {
            messages.add(message);
            messagesAdapter.notifyDataSetChanged();
        });
        //adds new message to database
        bind.messageEditText.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                String text = bind.messageEditText.getText().toString();
                Constants.DATABASE.child("messages/"+uid+"/"+path+"/messagelist").push()
                        .setValue(new Message(Constants.USER,text,System.currentTimeMillis()));
                bind.messageEditText.setText("");
                return true;
            }
            return false;
        });
        return bind.getRoot();
    }
}
