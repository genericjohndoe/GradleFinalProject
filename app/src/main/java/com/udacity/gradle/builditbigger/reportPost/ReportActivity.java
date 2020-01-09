package com.udacity.gradle.builditbigger.reportPost;


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
                            Constants.DATABASE.child("reportedposts/"+ccId+"/"+postId+"/list")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long num = dataSnapshot.getChildrenCount();
                                    if (num >= 3){
                                        //initiate voting process
                                        //send notification to immediate social network
                                        //based on results send to entire network (or not)
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
