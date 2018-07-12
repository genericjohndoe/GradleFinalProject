package com.udacity.gradle.builditbigger.SignInTutorial.UserName;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Constants.FlagEmojiMap;
import com.udacity.gradle.builditbigger.Interfaces.SetFlag;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.SignInTutorial.UserName.PickCountry.CountriesPopUpDialogFragment;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class UserNameActivity extends AppCompatActivity implements SetFlag {
    private boolean proceed = false;
    private TextView countryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        EditText editText = findViewById(R.id.user_name_editText);
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
                       proceed = true;
                   } else {
                       imageView.setBackground(getDrawable(R.drawable.ic_close_24dp));
                       proceed = false;
                   }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.continue_button).setOnClickListener(view ->{
            if (proceed){
                //todo switch out with generic icon
                String url = "https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg";
                Constants.USER = new HilarityUser(editText.getText().toString(), url, Constants.UID);
                Constants.DATABASE.child("users/"+Constants.UID).setValue(Constants.USER, (databaseError, databaseReference) -> {
                    if (databaseError == null){
                        startActivity(new Intent(this, HilarityActivity.class));
                    }
                });
            }
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
