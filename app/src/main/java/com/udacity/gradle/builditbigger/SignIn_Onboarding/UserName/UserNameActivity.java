package com.udacity.gradle.builditbigger.SignIn_Onboarding.UserName;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Constants.FlagEmojiMap;
import com.udacity.gradle.builditbigger.Interfaces.SetFlag;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SignIn_Onboarding.ProfilePicture.ProfilePictureActivity;
import com.udacity.gradle.builditbigger.SignIn_Onboarding.UserName.PickCountry.CountriesPopUpDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class UserNameActivity extends AppCompatActivity implements SetFlag {
    private boolean nameProceed = false;
    private TextView countryTextView;
    private boolean dateProceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        EditText editText = findViewById(R.id.user_name_editText);
        TextView dobEditText = findViewById(R.id.dob_editText);
        ImageView imageView = findViewById(R.id.status_imageView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = "" + s;
                Constants.FIRESTORE.collection("users").whereEqualTo("userName", userName).get().addOnSuccessListener(queryDocumentSnapshots -> {
                   if (queryDocumentSnapshots.getDocuments().size() == 0 && !userName.contains(" ") && !userName.equals("")){
                       imageView.setBackground(getDrawable(R.drawable.ic_check_24dp));
                       imageView.setContentDescription(getString(R.string.user_name_valid));
                       nameProceed = true;
                   } else {
                       imageView.setBackground(getDrawable(R.drawable.ic_close_24dp));
                       imageView.setContentDescription(getString(R.string.user_name_invalid));
                       nameProceed = false;
                   }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.continue_button).setOnClickListener(view ->{
            if (nameProceed && dateProceed){
                //todo switch out with generic icon
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date d = f.parse(dobEditText.getText().toString());
                    long milliseconds = d.getTime();
                    Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/demographic/dob").setValue(milliseconds);
                    Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/demographic/country").setValue(getResources().getConfiguration().locale.getCountry());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Constants.USER = new HilarityUser(editText.getText().toString(), null, Constants.UID);
                Constants.DATABASE.child("users/"+Constants.UID).setValue(Constants.USER, (databaseError, databaseReference) -> {
                    if (databaseError == null) startActivity(new Intent(this, ProfilePictureActivity.class));
                });
            }
        });
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);
        dobEditText.setText(day +"/"+month+"/"+year);
        dobEditText.setOnClickListener(view ->{
            DatePickerDialog.OnDateSetListener listener =
                    (DatePicker view2, int year2, int monthOfYear, int dayOfMonth) -> {
                    dobEditText.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year2);
                    if (year != year2) dateProceed = true;
            };
            new SpinnerDatePickerDialogBuilder()
                    .context(this)
                    .callback(listener)
                    .showTitle(true)
                    .defaultDate(year, month-1, day)
                    .maxDate(year, month-1, day)
                    .build()
                    .show();
        });
        String country = getResources().getConfiguration().locale.getCountry();
        countryTextView = findViewById(R.id.country_textView);
        countryTextView.setText(FlagEmojiMap.getInstance().get(country));
        countryTextView.setOnClickListener(view -> {
            CountriesPopUpDialogFragment.getInstance(this).show(getSupportFragmentManager(), "countries");
        });
    }

    @Override
    public void setFlag(String flag) {
        countryTextView.setText(flag);
    }
}
