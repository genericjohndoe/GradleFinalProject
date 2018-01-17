package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;
import com.pchmn.materialchips.views.ChipsInputEditText;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUser;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class ComposeMessageFragment extends Fragment implements CreateChip {

    //todo connect edit text and other recycler view show that before typing user sees
    //todo chron list of people messaged, then people in network, then queries master list of user
    //todo once user is picked from bottom recyclerview, add user chips to top recycler view

    ChipsInput chipsInput;
    RecyclerView recyclerView;
    List<HilarityUser> chipList;
    List<Message> messageList;
    EditText editText;

    UsersToMessageAdapter usersToMessageAdapter;
    MessagesAdapter messagesAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chipList = new ArrayList<>();
        messageList = new ArrayList<>();
        usersToMessageAdapter = new UsersToMessageAdapter(chipList,this, getActivity());
        messagesAdapter = new MessagesAdapter(messageList ,getActivity());
        Constants.DATABASE.child("network/"+Constants.UID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        chipList.add(dataSnapshot.getValue(HilarityUser.class));
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_compose_message, container, false);
        chipsInput = root.findViewById(R.id.chips_input);
        //todo get access to edit text, connect to text watcher
        recyclerView = root.findViewById(R.id.user_message_recyclerview);
        recyclerView.setAdapter(usersToMessageAdapter);
        editText = root.findViewById(R.id.incoming_message_edittext);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    recyclerView.setAdapter(messagesAdapter);
                } else {
                    //todo check to sent message has already been set
                    recyclerView.setAdapter(usersToMessageAdapter);
                }
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //todo add code for submission to database
                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        return root;
    }


    class HilarityUserChip implements ChipInterface{
        HilarityUser hilarityUser;

        HilarityUserChip(HilarityUser hilarityUser){
            this.hilarityUser = hilarityUser;
        }

        @Override
        public Drawable getAvatarDrawable() {
            return null;
        }

        @Override
        public Object getId() {
            return null;
        }

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public String getLabel() {
            return hilarityUser.getUserName();
        }

        @Override
        public Uri getAvatarUri() {
            return Uri.parse(hilarityUser.getUrlString());
        }
    }

    @Override
    public void addChipView(HilarityUser hu) {
        chipsInput.addChip(new HilarityUserChip(hu));
    }
}
