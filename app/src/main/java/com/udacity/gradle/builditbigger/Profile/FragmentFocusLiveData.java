package com.udacity.gradle.builditbigger.Profile;

import android.arch.lifecycle.MutableLiveData;

public class FragmentFocusLiveData extends MutableLiveData<Integer> {
    private static FragmentFocusLiveData fragmentFocusLiveData;

    public static FragmentFocusLiveData getFragmentFocusLiveData() {
        if (fragmentFocusLiveData == null) fragmentFocusLiveData = new FragmentFocusLiveData();
        return fragmentFocusLiveData;
    }
}
