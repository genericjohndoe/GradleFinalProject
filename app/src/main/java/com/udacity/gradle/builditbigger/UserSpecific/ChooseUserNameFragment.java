package com.udacity.gradle.builditbigger.UserSpecific;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by joeljohnson on 10/3/17.
 */

public class ChooseUserNameFragment extends SlideFragment {
    EditText editText;
    DatabaseReference userDatabaseReference;
    FirebaseUser firebaseUser;
    boolean userNameCreated;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userNameCreated = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choose_username, container, false);
        userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        editText = view.findViewById(R.id.username_edittext);
        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.primary;
    }

    @Override
    public int buttonsColor() {
        return R.color.accent;
    }

    @Override
    public boolean canMoveFurther() {
        return checkName();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Must Pick Viable User Name First";
    }

    private Boolean checkName(){
        if (editText.getText().toString().length() > 0){
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final String userName = editText.getText().toString();
            Query query = userDatabaseReference.child("userlist").limitToFirst(1).equalTo(userName).orderByValue();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("joke", "onDataChange called");
                    if (dataSnapshot.getChildrenCount() == 0){
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        Set<String> set = sharedPref.getStringSet(getString(R.string.preference_saved_languages_set), null);
                        List<String> languageList = new ArrayList<String>();
                        languageList.addAll(set);
                        userDatabaseReference.child("users").child(firebaseUser.getUid())
                                .setValue(new HilarityUser(userName,"www.google.com", languageList));
                        userDatabaseReference.getRoot().child("following/"+firebaseUser.getUid()+"/num").setValue(0);
                        userDatabaseReference.getRoot().child("followers/"+firebaseUser.getUid()+"/num").setValue(0);
                        userDatabaseReference.getRoot().child("userlist/"+firebaseUser.getUid()).setValue(userName);
                        Log.i("joke", "no username found");
                        userNameCreated = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
        return userNameCreated;
    }
}
