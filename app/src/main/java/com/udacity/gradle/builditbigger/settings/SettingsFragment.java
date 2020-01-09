package com.udacity.gradle.builditbigger.settings;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSettingsBinding;
import com.udacity.gradle.builditbigger.settings.contentCreatorSettings.ContentCreatorActivity;
import com.udacity.gradle.builditbigger.settings.notificationSettings.NotificationSettingsActivity;
import com.udacity.gradle.builditbigger.settings.paymentSettings.PaymentActivity;
import com.udacity.gradle.builditbigger.settings.userSettings.UserSettingsActivity;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
