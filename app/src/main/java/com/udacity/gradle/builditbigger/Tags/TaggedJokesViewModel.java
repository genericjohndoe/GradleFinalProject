package com.udacity.gradle.builditbigger.Tags;

import android.arch.lifecycle.ViewModel;

/**
 * Created by joeljohnson on 3/18/18.
 */

public class TaggedJokesViewModel extends ViewModel {
    private TaggedJokesLiveData taggedJokesLiveData;

    public TaggedJokesViewModel(String tag){
        taggedJokesLiveData = new TaggedJokesLiveData(tag);
    }

    public TaggedJokesLiveData getTaggedJokesLiveData() {
        return taggedJokesLiveData;
    }
}
