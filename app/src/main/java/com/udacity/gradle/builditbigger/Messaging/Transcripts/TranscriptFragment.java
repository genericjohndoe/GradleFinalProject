package com.udacity.gradle.builditbigger.Messaging.Transcripts;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.udacity.gradle.builditbigger.Interfaces.ActivityForResult;
import com.udacity.gradle.builditbigger.Interfaces.IntentCreator;
import com.udacity.gradle.builditbigger.Messaging.MediaDialog.AddMediaDialog;
import com.udacity.gradle.builditbigger.Messaging.MediaDialog.ChooseMediaDialog;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Models.Message;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;
import com.udacity.gradle.builditbigger.NewPost.NewPostActivity2;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentTranscriptBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TranscriptFragment class shows messages exchanged between users
 */

public class TranscriptFragment extends Fragment implements IntentCreator, ActivityForResult {
    private String uid;
    private String path;
    private String[] usersUidList;
    private List<HilarityUser> hilarityUsers;
    private FragmentTranscriptBinding bind;
    private DialogFragment newMedia;
    private ChooseMediaDialog chooseMediaDialog;

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_transcript,container,false);
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
            List<String> textList = new ArrayList<>();
            textList.add(text);
            sendMessage(textList);
        });

        bind.mediaButton.setOnClickListener(view ->{
            //todo show media dialog for different options
            //todo start different activity based off whats pressed
            chooseMediaDialog = ChooseMediaDialog.getInstance(this);
            chooseMediaDialog.show(getActivity().getSupportFragmentManager(),"new media");
            //newMedia = AddMediaDialog.getInstance(this);
            //newMedia.show(getActivity().getSupportFragmentManager(),"new media");
        });

        return bind.getRoot();
    }

    @Override
    public void createIntent(String filepath, String number) {
        String dbPath = "users/" + Constants.UID + "/media/" + Constants.getCurrentDateAndTime() + "." + Constants.getExtension(filepath);
        Constants.STORAGE.child(dbPath)
                .putFile(Uri.fromFile(new File(filepath)))
                .addOnFailureListener(exception -> {})
                .addOnSuccessListener(taskSnapshot -> {
                    List<String> messageInfo = new ArrayList<>();
                    messageInfo.add(dbPath);
                    messageInfo.add(taskSnapshot.getUploadSessionUri().toString());
                    sendMessage(messageInfo);
                });
        chooseMediaDialog.dismiss();
    }

    private void sendMessage(List<String> messageContents){
        DatabaseReference db = Constants.DATABASE.child("messages/"+uid+"/"+path+"/messagelist").push();
        Message message = new Message(Constants.USER,messageContents,System.currentTimeMillis(), db.getKey(), true);
        db.setValue(message, (databaseError, databaseReference) -> {
            if (databaseError == null) {
                Constants.DATABASE.child("transcriptpreviews/"+uid+"/"+path+"/message")
                        .setValue(message);
            }
        });
        bind.messageEditText.setText("");
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(bind.messageEditText.getWindowToken(), 0);
    }

    @Override
    public void activityForResult(int num) {
        Intent intent = new Intent(getActivity(), NewPostActivity2.class);
        intent.putExtra("posttype", num);
        intent.putExtra("number", "-1");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("newmediatest", "OAR called");
        if (requestCode == 1){
            Log.i("newmediatest", ""+resultCode);
            String filepath = data.getStringExtra("filepath");
            Log.i("newmediatest", filepath);
            createIntent(filepath, null);
        }
    }
}
