package com.udacity.gradle.builditbigger.Jokes;

import com.udacity.gradle.builditbigger.Models.Joke;

/**
 * Created by joeljohnson on 3/1/18.
 */

public class ViewHolderViewModel {
    private ProfileImgLiveData profileImgLiveData;
    private NumLikesLiveData numLikesLiveData;
    private NumCommentsLiveData numCommentsLiveData;
    private IsLikedLiveData isLikedLiveData;
    private UserNameLiveData userNameLiveData;

    public ViewHolderViewModel(Joke joke){
        profileImgLiveData = new ProfileImgLiveData(joke.getUID());
        numLikesLiveData = new NumLikesLiveData(joke.getUID(),joke.getPushId());
        numCommentsLiveData = new NumCommentsLiveData(joke.getUID(),joke.getPushId());
        isLikedLiveData = new IsLikedLiveData(joke.getUID(),joke.getPushId());
        userNameLiveData = new UserNameLiveData(joke.getUID());
    }

    public ProfileImgLiveData getProfileImgLiveData() {
        return profileImgLiveData;
    }

    public NumLikesLiveData getNumLikesLiveData() {
        return numLikesLiveData;
    }

    public NumCommentsLiveData getNumCommentsLiveData() {
        return numCommentsLiveData;
    }

    public IsLikedLiveData getIsLikedLiveData() {
        return isLikedLiveData;
    }

    public UserNameLiveData getUserNameLiveData() {
        return userNameLiveData;
    }
}
