package com.udacity.gradle.builditbigger.Settings.UserSettings;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentUserSettingsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingsFragment extends Fragment {
    private boolean userNameValidated;
    private ValueEventListener valueEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentUserSettingsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_user_settings, container, false);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    bind.userNameValidImageview.setImageResource(R.drawable.ic_close_24dp);
                    userNameValidated = false;
                } else {
                    bind.userNameValidImageview.setImageResource(R.drawable.ic_check_24dp);
                    userNameValidated = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };

        bind.userNameEditText.setText(Constants.USER.getUserName());
        bind.userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Constants.DATABASE.child("userlist").orderByValue().equalTo(""+s)
                        .addValueEventListener(valueEventListener);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        bind.userNameEditText.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            String newName = bind.userNameEditText.getText().toString();
            if (!hasFocus && userNameValidated &&
                    !Constants.USER.getUserName().equals(newName)) {
                Constants.DATABASE.child("users/"+Constants.UID +"/userName").setValue(newName, (databaseError, databaseReference) -> {
                    if (databaseError == null){
                        Constants.USER.setUserName(newName);
                        Constants.DATABASE.child("userlist").removeEventListener(valueEventListener);
                    }
                });
            }
        });
        bind.selectNewPhotoButton.setOnClickListener(view -> {
            //start new activity to take/choose photo or gif
            //delete or overwrite old profile pic in storage
            //change on client side
        });

        bind.profileTaglineEditText.setText(Constants.TAGLINE);
        bind.profileTaglineEditText.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus){
                Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/tagline")
                        .setValue(bind.profileTaglineEditText.getText().toString());
            }
        });
        //make autotranslate machine learning album
        bind.autoTranslateSwitch.setChecked(true);
        bind.autoTranslateSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/autotranslate").setValue(isChecked);
        });

        //populate adapter with list of countries
        bind.nationAutoCompleteTextView.setAdapter(null);
        return bind.getRoot();
    }

}
