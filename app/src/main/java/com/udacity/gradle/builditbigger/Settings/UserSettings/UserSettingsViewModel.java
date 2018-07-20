package com.udacity.gradle.builditbigger.Settings.UserSettings;

import android.arch.lifecycle.ViewModel;

public class UserSettingsViewModel extends ViewModel {
    private AutoTranslateLiveData autoTranslateLiveData;
    private TaglineLiveData taglineLiveData;

    public AutoTranslateLiveData getAutoTranslateLiveData() {
        if (autoTranslateLiveData ==  null) autoTranslateLiveData = new AutoTranslateLiveData();
        return autoTranslateLiveData;
    }

    public TaglineLiveData getTaglineLiveData() {
        if (taglineLiveData == null) taglineLiveData = new TaglineLiveData();
        return taglineLiveData;
    }
}
