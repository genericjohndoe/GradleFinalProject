package com.udacity.gradle.builditbigger.Settings.UserSettings;

import android.arch.lifecycle.ViewModel;

public class UserSettingsViewModel extends ViewModel {
    private AutoTranslateLiveData autoTranslateLiveData;
    private TaglineLiveData taglineLiveData;
    private CountryLiveData countryLiveData;
    private DobLiveData dobLiveData;

    public AutoTranslateLiveData getAutoTranslateLiveData() {
        if (autoTranslateLiveData ==  null) autoTranslateLiveData = new AutoTranslateLiveData();
        return autoTranslateLiveData;
    }

    public TaglineLiveData getTaglineLiveData() {
        if (taglineLiveData == null) taglineLiveData = new TaglineLiveData();
        return taglineLiveData;
    }

    public CountryLiveData getCountryLiveData() {
        if (countryLiveData == null) countryLiveData = new CountryLiveData();
        return countryLiveData;
    }

    public DobLiveData getDobLiveData() {
        if (dobLiveData == null) dobLiveData = new DobLiveData();
        return dobLiveData;
    }
}
