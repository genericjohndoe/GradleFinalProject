package com.udacity.gradle.builditbigger.SignInTutorial;

import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * allows user to choose user name
 */

public class ChooseUserNameFragment extends SlideFragment {
    EditText editText;
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

    private Boolean checkName() {
        if (editText.getText().toString().length() > 0) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final String userName = editText.getText().toString();
            Query query = Constants.DATABASE.child("userlist").limitToFirst(1).equalTo(userName).orderByValue();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("joke", "onDataChange called");
                    if (dataSnapshot.getChildrenCount() == 0) {
                        List<String> languageList = new ArrayList<String>();
                        languageList.add(Locale.getDefault().getDisplayLanguage());
                        Constants.DATABASE.child("users/" + firebaseUser.getUid())
                                .setValue(new HilarityUser(userName, "https://developer.android.com/_static/2f20c0c6d8/images/android/touchicon-180.png",firebaseUser.getUid()));
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
