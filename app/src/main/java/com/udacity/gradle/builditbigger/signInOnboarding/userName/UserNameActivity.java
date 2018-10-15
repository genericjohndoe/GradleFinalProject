package com.udacity.gradle.builditbigger.signInOnboarding.userName;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.constants.FlagEmojiMap;
import com.udacity.gradle.builditbigger.interfaces.SetFlag;
import com.udacity.gradle.builditbigger.models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.signInOnboarding.profilePicture.ProfilePictureActivity;
import com.udacity.gradle.builditbigger.signInOnboarding.userName.PickCountry.CountriesPopUpDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * the activity allows user to select user name, birth date, and gender (optional)
 */
public class UserNameActivity extends AppCompatActivity implements SetFlag {
    private boolean nameProceed = false;
    private TextView countryTextView;
    private boolean dateProceed = false;
    private String twoDigit = getResources().getConfiguration().locale.getCountry();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        EditText editText = findViewById(R.id.user_name_editText);
        TextView dobEditText = findViewById(R.id.dob_editText);
        ImageView imageView = findViewById(R.id.status_imageView);

        //make edittext reactive to input, when user selected unused name green check mark shows
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

        //submit all relevant information to the database
        findViewById(R.id.continue_button).setOnClickListener(view ->{
            if (nameProceed && dateProceed){
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date d = f.parse(dobEditText.getText().toString());
                    long milliseconds = d.getTime();
                    Constants.DATABASE.child("cloudsettings/" + Constants.UID + "/demographic/dob").setValue(milliseconds);
                    Constants.DATABASE.child("cloudsettings/" + Constants.UID + "/demographic/country").setValue(twoDigit);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Constants.USER = new HilarityUser(editText.getText().toString(), null, Constants.UID);
                Constants.DATABASE.child("users/"+Constants.UID).setValue(Constants.USER, (databaseError, databaseReference) -> {
                    if (databaseError == null) startActivity(new Intent(this, ProfilePictureActivity.class));
                });
                int selected = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
                if (selected != -1) {
                    RadioButton button = findViewById(selected);
                    Constants.DATABASE.child("cloudsettings/" + Constants.UID + "/demographic/gender").setValue(button.getText());
                }
            }
        });

        //set up the dialog for birthday selection
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);
        dobEditText.setText(day +"/"+month+"/"+year);
        dobEditText.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener listener =
                    (DatePicker view2, int year2, int monthOfYear, int dayOfMonth) -> {
                    dobEditText.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year2);
                    if (year != year2) dateProceed = true;
            };
            new SpinnerDatePickerDialogBuilder()
                    .context(this)
                    .callback(listener)
                    .showTitle(true)
                    .defaultDate(year-16, month-1, day)
                    .maxDate(year-16, month-1, day)
                    .build()
                    .show();
        });

        //select flag based on information from phone
        String country = getResources().getConfiguration().locale.getCountry();
        countryTextView = findViewById(R.id.country_textView);
        countryTextView.setText(FlagEmojiMap.getInstance().get(country));
        countryTextView.setOnClickListener(view -> {
            CountriesPopUpDialogFragment.getInstance(this).show(getSupportFragmentManager(), "countries");
        });


    }

    /**
     * callback receives information from dialog after item ws pressed
     * @param flag the emoji seen in the UI
     * @param isoCode the two digit isocode used on the back end
     */
    @Override
    public void setFlag(String flag, String isoCode) {
        countryTextView.setText(flag);
        twoDigit = isoCode;
    }
}
