package com.udacity.gradle.builditbigger.reportPost;


import android.app.Notification;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.richTextEditor.RichEditor;
import com.udacity.gradle.builditbigger.richTextEditor.RichEditorToolBar;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        RichEditorToolBar toolBar = findViewById(R.id.richEditorToolBar2);
        toolBar.setRichEditor(findViewById(R.id.editor));
        RichEditor editor = ((RichEditor) findViewById(R.id.editor));
        editor.setEditorFontColor("#369F77");
        String ccId = getIntent().getStringExtra("ccID");
        String postId = getIntent().getStringExtra("pushId");
        findViewById(R.id.submit_imageButton).setOnClickListener(view -> {
            Constants.DATABASE.child("reportedposts/"+ccId+"/"+postId+"/list/"+Constants.UID).setValue(editor.getHtml(),
                    (databaseError, databaseReference) -> {
                        if (databaseError == null){
                            Constants.DATABASE.child("reportedposts/"+ccId+"/"+postId+"/num")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long num = dataSnapshot.getChildrenCount();
                                    if (num >= 3){

                                    } else {
                                        Constants.DATABASE.child("reportedposts/"+ccId+"/"+postId+"/num").setValue(num+1);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });

                        }
                    });
            //start cloud function if hasn't already done so
            //sends notification to cc for 36 h count down
        });
    }
}
//todo: test code that sends notification to original content creator once there are atleast 3 complaints
//todo: set up code to send notification to entire network when rebuttal is sent
//todo: check to see if rebuttal hasn't been sent on client side, if not send note to network anyway
//todo: note opens to post, then proceeds to complaint and rebuttal (if applicable), then vote
//todo: set voting to be open for 48 h (open own server, raspberry pi), if majority vote yes, remove post
//todo: if user gets more than 3 removed posts in a 12 month span, users account is removed

