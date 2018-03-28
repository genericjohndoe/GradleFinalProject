package com.udacity.gradle.builditbigger.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.search_framelayout, SearchFragment.newInstance())
                .commit();
    }
}
