package com.udacity.gradle.builditbigger.SignInTutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class UserNameActivity extends AppCompatActivity {
    private boolean proceed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        EditText editText = findViewById(R.id.user_name_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Constants.FIRESTORE.collection("users").whereEqualTo("userName", s).get().addOnSuccessListener(queryDocumentSnapshots -> {
                   if (queryDocumentSnapshots.getDocuments().size() == 0){
                       findViewById(R.id.status_imageView).setBackground(getDrawable(R.drawable.fui_done_check_mark));
                       proceed = true;
                   } else {
                       findViewById(R.id.status_imageView).setBackground(getDrawable(R.drawable.fui_idp_button_background_email));
                       proceed = false;
                   }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.continue_button).setOnClickListener(view ->{
            if (proceed){
                String url = "https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg";
                Constants.USER = new HilarityUser(editText.getText().toString(), url, Constants.UID);
                Constants.DATABASE.child("users/"+Constants.UID).setValue(Constants.USER, (databaseError, databaseReference) -> {
                    if (databaseError == null){
                        startActivity(new Intent(this, HilarityActivity.class));
                    }
                });
            }
        });

    }
}
