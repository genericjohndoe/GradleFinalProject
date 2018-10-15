package com.udacity.gradle.builditbigger.settings.notificationSettings;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNotificationSettingsBinding;

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
        if (getArguments() != null) uid = getArguments().getString(getString(R.string.uid));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentNotificationSettingsBinding bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_notification_settings, container, false);

        bind.followersNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/notifications/followerNotification").setValue(isChecked);
        });
        bind.forumRepliesNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/notifications/forumReplyNotification").setValue(isChecked);
        });
        bind.mentionsNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/notifications/mentionsNotification").setValue(isChecked);
        });
        bind.messageNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Constants.DATABASE.child("cloudsettings/"+Constants.UID+"/notifications/messagesNotification").setValue(isChecked);
        });
        return bind.getRoot();
    }

}
