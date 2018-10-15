package com.udacity.gradle.builditbigger.jokes;

import com.udacity.gradle.builditbigger.models.Comment;
import com.udacity.gradle.builditbigger.models.ForumQuestion;
import com.udacity.gradle.builditbigger.models.ForumReply;
import com.udacity.gradle.builditbigger.models.Post;

/**
 * Class provides post information to viewholder
 */

public class ViewHolderViewModel {
    private ProfileImgLiveData profileImgLiveData;
    private NumLikesLiveData numLikesLiveData;
    private NumCommentsLiveData numCommentsLiveData;
    private IsLikedLiveData isLikedLiveData;
    private UserNameLiveData userNameLiveData;

    public ViewHolderViewModel(Post joke){
        profileImgLiveData = new ProfileImgLiveData(joke.getUID());
        numLikesLiveData = new NumLikesLiveData(joke.getUID(),joke.getPushId());
        numCommentsLiveData = new NumCommentsLiveData(joke.getUID(),joke.getPushId());
        isLikedLiveData = new IsLikedLiveData(joke.getUID(),joke.getPushId());
        userNameLiveData = new UserNameLiveData(joke.getUID());
    }

    public ViewHolderViewModel(ForumQuestion forumQuestion){
        userNameLiveData = new UserNameLiveData(forumQuestion.getHilarityUserUID());
    }

    public ViewHolderViewModel(ForumReply reply){
        userNameLiveData = new UserNameLiveData(reply.getHilarityUserUID());
    }

    public ViewHolderViewModel(Comment comment){
        userNameLiveData = new UserNameLiveData(comment.getHilarityUserUID());
        profileImgLiveData = new ProfileImgLiveData(comment.getHilarityUserUID());
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
