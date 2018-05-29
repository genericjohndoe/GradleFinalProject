package com.udacity.gradle.builditbigger.Messaging.ComposeMessage;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Database.SearchViewModel;
import com.udacity.gradle.builditbigger.Database.SearchViewModelFactory;
import com.udacity.gradle.builditbigger.Interfaces.CreateChip;
import com.udacity.gradle.builditbigger.Interfaces.FilterRecyclerView;
import com.udacity.gradle.builditbigger.Interfaces.IntentCreator;
import com.udacity.gradle.builditbigger.Messaging.MediaDialog.AddMediaDialog;
import com.udacity.gradle.builditbigger.Messaging.Transcripts.TranscriptActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.Models.Message;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentComposeMessageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * to be shown when user is picking people to message
 */
public class ComposeMessageFragment extends Fragment implements CreateChip, FilterRecyclerView, IntentCreator {
    //todo chron list of people messaged, then people in network, then queries master list of user

    private List<HilarityUser> networkChipList;
    private String uid;
    private FragmentComposeMessageBinding bind;
    private UsersToMessageAdapter usersToMessageAdapter;
    private List<HilarityUser> hilarityUsers;//list of intended users
    private IntendedRecipientAdapter intendedRecipientAdapter;

    public static ComposeMessageFragment newInstance(String uid) {
        ComposeMessageFragment composeMessageFragment = new ComposeMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        composeMessageFragment.setArguments(bundle);
        return composeMessageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChipList = new ArrayList<>();
        hilarityUsers = new ArrayList<>();
        usersToMessageAdapter = new UsersToMessageAdapter(networkChipList, this, getActivity());
        intendedRecipientAdapter = new IntendedRecipientAdapter(hilarityUsers, getActivity(), this);
        uid = getArguments().getString("uid");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_compose_message, container, false);

        bind.recipientRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bind.recipientRecyclerview.setAdapter(intendedRecipientAdapter);

        bind.userMessageRecyclerview.setAdapter(usersToMessageAdapter);
        bind.userMessageRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        bind.userMessageRecyclerview.requestFocus();
        bind.userMessageRecyclerview.setOnTouchListener((view, event) -> bind.userMessageRecyclerview.requestFocus());

        bind.sendButton.setOnClickListener(view -> {
            String text = bind.incomingMessageEdittext.getText().toString();
            List<String> textList = new ArrayList<>();
            textList.add(text);
            sendMessage(textList);
        });


        bind.incomingMessageEdittext.setOnFocusChangeListener((view, hasFocus) -> {
            usersToMessageAdapter.setHilarityUserList(new ArrayList<>());
            usersToMessageAdapter.notifyDataSetChanged();
        });
        SearchViewModel searchViewModel = ViewModelProviders.of(ComposeMessageFragment.this, new SearchViewModelFactory(getActivity().getApplication())).get(SearchViewModel.class);
        searchViewModel.getTempUserLiveData().observe(this, (user) -> {
            if (!networkChipList.contains(user)) {
                networkChipList.add(user);
                usersToMessageAdapter.notifyDataSetChanged();
            }
        });
        bind.addMediaImagebutton.setOnClickListener(view -> {
            DialogFragment newMedia = AddMediaDialog.getInstance(this);
            newMedia.show(getActivity().getSupportFragmentManager(), "new media");
        });
        return bind.getRoot();
    }


    @Override
    public void addChipView(HilarityUser hu) {
        hilarityUsers.add(hu);
        intendedRecipientAdapter.notifyDataSetChanged();
    }

    @Override
    public List<HilarityUser> getSelectedUsers() {
        return hilarityUsers;
    }

    @Override
    public void removeChipView(HilarityUser hu) {
        hilarityUsers.remove(hu);
        intendedRecipientAdapter.notifyDataSetChanged();
    }

    @Override
    public void filter(String input) {
        usersToMessageAdapter.setHilarityUserList(filter(input, networkChipList));
        usersToMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestFocus() {
        usersToMessageAdapter.setHilarityUserList(networkChipList);
    }

    private List<HilarityUser> filter(String str, List<HilarityUser> list) {
        ArrayList<HilarityUser> newList = new ArrayList<>();
        for (HilarityUser user : list) {
            if (user.getUserName().contains(str)) newList.add(user);

        }
        return newList;
    }

    @Override
    public void createIntent(String filepath, String number) {
        String dbPath = "users/" + Constants.UID + "/images/" + Constants.getCurrentDateAndTime() + ".png";
        Constants.STORAGE.child(dbPath)
                .putFile(Uri.fromFile(new File(filepath)))
                .addOnFailureListener(exception -> {})
                .addOnSuccessListener(taskSnapshot -> {
                    List<String> messageInfo = new ArrayList<>();
                    messageInfo.add(dbPath);
                    messageInfo.add(taskSnapshot.getUploadSessionUri().toString());
                    sendMessage(messageInfo);
                });
    }

    private void sendMessage(List<String> messageContent) {
        List<String> sendTo = new ArrayList<>();
        for (HilarityUser chip : hilarityUsers) {
            sendTo.add(chip.getUid());
        }
        sendTo.add(Constants.UID);
        Collections.sort(sendTo);
        String path = sendTo.toString().substring(1, sendTo.toString().length() - 1);
        DatabaseReference db = Constants.DATABASE.child("messages/" + Constants.UID + "/" + path + "/messagelist").push();
        Message message = new Message(Constants.USER, messageContent, System.currentTimeMillis(), db.getKey(), true);
        hilarityUsers.add(Constants.USER);
        db.setValue(message, ((databaseError, databaseReference) -> {
            if (databaseError == null && hilarityUsers != null) {
                Constants.DATABASE.child("transcriptpreviews/" + Constants.UID + "/" + path)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Constants.DATABASE.child("transcriptpreviews/" + Constants.UID + "/" + path + "/message")
                                            .setValue(message);
                                } else {
                                    Constants.DATABASE.child("transcriptpreviews/" + Constants.UID + "/" + path)
                                            .setValue(new TranscriptPreview(message, hilarityUsers, path, true));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                Intent intent = new Intent(getActivity(), TranscriptActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                getActivity().finish();
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(bind.incomingMessageEdittext.getWindowToken(), 0);
            }
        }));
    }
}
