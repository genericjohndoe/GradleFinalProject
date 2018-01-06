package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.util.Rfc822Tokenizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUser;


/**
 *
 */
public class ComposeMessageFragment extends Fragment {

    RecipientEditTextView recipientEditTextView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.DATABASE.child("network/"+Constants.UID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HilarityUser hu = dataSnapshot.getValue(HilarityUser.class);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_compose_message, container, false);
        recipientEditTextView = root.findViewById(R.id.chips_input);
        recipientEditTextView.setTokenizer(new Rfc822Tokenizer());
        recipientEditTextView.setAdapter(new BaseRecipientAdapter(getActivity()));
        return root;
    }


}
