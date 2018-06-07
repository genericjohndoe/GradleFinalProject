package com.udacity.gradle.builditbigger.Settings;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.Settings.ContentCreatorSettings.ContentCreatorActivity;
import com.udacity.gradle.builditbigger.Settings.NotificationSettings.NotificationSettingsActivity;
import com.udacity.gradle.builditbigger.Settings.PaymentSettings.PaymentActivity;
import com.udacity.gradle.builditbigger.Settings.UserSettings.UserSettingsActivity;
import com.udacity.gradle.builditbigger.databinding.FragmentSettingsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //todo check to see if user is in beta program, then set value
        FragmentSettingsBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false);
        bind.userSettingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
            startActivity(intent);
        });
        bind.notificationSettingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
            startActivity(intent);
        });
        bind.paymentButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            startActivity(intent);
        });
        bind.contentCreatorButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ContentCreatorActivity.class);
            startActivity(intent);
        });
        bind.betaProgramSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //todo check to see if space is available, then response appropriately
        });
        return bind.getRoot();
    }

}
