package com.udacity.gradle.builditbigger.settings.userSettings;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.constants.FlagEmojiMap;
import com.udacity.gradle.builditbigger.interfaces.SetFlag;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.signInOnboarding.profilePicture.ProfilePictureActivity;
import com.udacity.gradle.builditbigger.signInOnboarding.userName.PickCountry.CountriesPopUpDialogFragment;
import com.udacity.gradle.builditbigger.databinding.FragmentUserSettingsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSettingsFragment extends Fragment implements SetFlag {
    //todo replace edittext with rainbow ring
    private boolean userNameValidated;
    private ValueEventListener valueEventListener;
    private FragmentUserSettingsBinding bind;
    private String tag = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_user_settings, container, false);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    bind.userNameValidImageview.setImageResource(R.drawable.ic_close_24dp);
                    userNameValidated = false;
                    bind.userNameValidImageview.setContentDescription(getString(R.string.user_name_invalid));
                } else {
                    bind.userNameValidImageview.setImageResource(R.drawable.ic_check_24dp);
                    userNameValidated = true;
                    bind.userNameValidImageview.setContentDescription(getString(R.string.user_name_valid));
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
            if (!hasFocus && userNameValidated && !Constants.USER.getUserName().equals(newName)) {
                Constants.DATABASE.child("users/"+Constants.UID +"/userName").setValue(newName, (databaseError, databaseReference) -> {
                    if (databaseError == null){
                        Constants.USER.setUserName(newName);
                        Constants.DATABASE.child("userlist").removeEventListener(valueEventListener);
                    }
                });
            }
        });
        bind.selectNewPhotoButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ProfilePictureActivity.class);
            startActivity(intent);
        });

        UserSettingsViewModel userSettingsViewModel = ViewModelProviders.of(this).get(UserSettingsViewModel.class);

        userSettingsViewModel.getAutoTranslateLiveData().observe(this, aBoolean -> {
            bind.autoTranslateSwitch.setChecked(aBoolean);
        });

        userSettingsViewModel.getTaglineLiveData().observe(this, tagline ->{
            bind.profileTaglineEditText.setText((tagline != null) ? tagline : getString(R.string.generic_profile_tagline));
            tag = tagline;
        });

        userSettingsViewModel.getCountryLiveData().observe(this, country ->{
            bind.flagTextView.setText(FlagEmojiMap.getInstance().get(country));
        });

        userSettingsViewModel.getDobLiveData().observe(this, dob -> {
            bind.ageTextView.setText(Constants.formattedTimeString(getActivity(), dob, true));
        });

        bind.profileTaglineEditText.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus && !bind.profileTaglineEditText.getText().toString().equals(tag)){
                Constants.DATABASE.child("users/"+Constants.UID+"/tagline")
                        .setValue(bind.profileTaglineEditText.getText().toString());
            }
        });


        bind.autoTranslateSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/autotranslate").setValue(isChecked);
        });
        bind.flagTextView.setOnClickListener(view -> {
            CountriesPopUpDialogFragment.getInstance(this).show(getActivity().getSupportFragmentManager(), "countries");
        });

        bind.ageTextView.setOnClickListener(view -> {
            SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");

            int defaultYear = 0;
            int defaultMonth = 0;
            int defaultDay = 0;

            try {
                Date date = formatter.parse(bind.ageTextView.getText().toString());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                defaultDay = calendar.get(Calendar.DAY_OF_MONTH);
                defaultMonth = calendar.get(Calendar.MONTH);
                defaultYear = calendar.get(Calendar.YEAR);
            } catch (ParseException e){}

            DatePickerDialog.OnDateSetListener listener =
                    (DatePicker view2, int year2, int monthOfYear, int dayOfMonth) -> {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.set(year2,monthOfYear, dayOfMonth);
                        long milliseconds = calendar2.getTimeInMillis();
                        Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/demographic/dob").setValue(milliseconds);
                    };

            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);

            new SpinnerDatePickerDialogBuilder()
                    .context(getActivity())
                    .callback(listener)
                    .showTitle(true)
                    .defaultDate(defaultYear, defaultMonth, defaultDay)
                    .maxDate(year-16, month, day)
                    .build()
                    .show();
        });

        return bind.getRoot();
    }

    @Override
    public void setFlag(String flag, String isoCode) {
        Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/demographic/country").setValue(isoCode);
    }
}
