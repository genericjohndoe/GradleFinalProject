package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by joeljohnson on 9/28/17.
 */

public class Profile extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        viewPager = root.findViewById(R.id.profile_view_pager);
        //viewPager.setAdapter();
        tabLayout = root.findViewById(R.id.profile_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }
}
