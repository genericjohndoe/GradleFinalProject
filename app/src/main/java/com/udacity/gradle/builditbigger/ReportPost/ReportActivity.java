package com.udacity.gradle.builditbigger.ReportPost;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;

import jp.wasabeef.richeditor.RichEditor;
import jp.wasabeef.richeditor.RichEditorToolBar;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        RichEditorToolBar toolBar = findViewById(R.id.richEditorToolBar2);
        toolBar.setRichEditor(findViewById(R.id.editor));
        ((RichEditor) findViewById(R.id.editor)).setEditorFontColor("#369F77");
        findViewById(R.id.submit_imageButton).setOnClickListener(view -> {
            //submit rationale to rtd
            //send user name, text from document,
            //update number of complaint on server side
            //update on per post basis
        });
    }
}