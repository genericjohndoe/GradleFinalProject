package com.udacity.gradle.builditbigger.Settings.NotificationSettings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationSettingsFragment extends Fragment {
    private String uid;

    public NotificationSettingsFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NotificationSettingsFragment.
     */
    public static NotificationSettingsFragment newInstance() {
        NotificationSettingsFragment fragment = new NotificationSettingsFragment();
        Bundle args = new Bundle();
        args.putString("uid", Constants.UID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) uid = getArguments().getString("uid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_settings, container, false);
    }

}
