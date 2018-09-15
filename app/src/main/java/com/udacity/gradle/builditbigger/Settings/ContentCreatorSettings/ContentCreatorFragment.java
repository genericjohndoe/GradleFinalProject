package com.udacity.gradle.builditbigger.Settings.ContentCreatorSettings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.charts.Map;
import com.anychart.enums.SelectionMode;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentCcBinding;

public class ContentCreatorFragment extends Fragment {

    public static ContentCreatorFragment getInstance(){
        return new ContentCreatorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentCcBinding bind = DataBindingUtil.inflate(inflater, R.layout.fragment_cc, container, false);
        Map map = AnyChart.map();
        map.geoData("anychart.maps.world");
        map.interactivity().selectionMode(SelectionMode.NONE);
        map.padding(0, 0, 0, 0);
        bind.worldMap.addScript("file:///android_asset/world.js");
        bind.worldMap.addScript("file:///android_asset/proj4.js");
        bind.worldMap.setChart(map);

        return bind.getRoot();
    }
}
