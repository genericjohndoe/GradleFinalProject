package com.udacity.gradle.builditbigger.Search;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.udacity.gradle.builditbigger.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        int position = getIntent().getIntExtra("position", 100);
        String searchTerm = getIntent().getStringExtra("searchTerm");
        Fragment fragment;
        if (position == 100){
            fragment = SearchFragment.newInstance();
        } else {
            fragment = SearchFragment.newInstance(position, searchTerm);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.search_framelayout, fragment)
                .commit();
    }
}
