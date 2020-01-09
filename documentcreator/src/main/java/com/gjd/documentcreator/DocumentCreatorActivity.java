package com.gjd.documentcreator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class DocumentCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_creator);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.document_creator_framelayout, DocumentCreatorFragment.newInstance())
                .commit();
    }
}
