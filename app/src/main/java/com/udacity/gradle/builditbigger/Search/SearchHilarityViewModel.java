package com.udacity.gradle.builditbigger.Search;

import android.arch.lifecycle.ViewModel;

import com.udacity.gradle.builditbigger.Search.SearchCollections.SearchCollectionsLiveData;
import com.udacity.gradle.builditbigger.Search.SearchGifPosts.SearchGifPostsFragment;
import com.udacity.gradle.builditbigger.Search.SearchImagePosts.SearchImagePostsLiveData;
import com.udacity.gradle.builditbigger.Search.SearchTags.SearchTagsLiveData;
import com.udacity.gradle.builditbigger.Search.SearchTextPosts.SearchTextPostsLiveData;
import com.udacity.gradle.builditbigger.Search.SearchUsers.SearchUserLiveData;
import com.udacity.gradle.builditbigger.Search.SearchVideoPosts.SearchVideoPostsLiveData;

/**
 * Created by joeljohnson on 3/16/18.
 */

public class SearchHilarityViewModel extends ViewModel {

    private SearchUserLiveData searchUserLiveData = new SearchUserLiveData();
    private SearchCollectionsLiveData searchCollectionsLiveData = new SearchCollectionsLiveData();
    private SearchTagsLiveData searchTagsLiveData = new SearchTagsLiveData();
    private SearchTextPostsLiveData searchTextPostsLiveData = new SearchTextPostsLiveData();
    private SearchImagePostsLiveData searchImagePostsLiveData = new SearchImagePostsLiveData();
    private SearchVideoPostsLiveData searchVideoPostsLiveData = new SearchVideoPostsLiveData();
    private SearchGifPostsFragment searchGifPostsFragment = new SearchGifPostsFragment();

    public SearchUserLiveData getSearchUserLiveData() {
        return searchUserLiveData;
    }
    public SearchCollectionsLiveData getSearchCollectionsLiveData() {return searchCollectionsLiveData;}
    public SearchTagsLiveData getSearchTagsLiveData() {return searchTagsLiveData;}
    public SearchTextPostsLiveData getSearchTextPostsLiveData() {return searchTextPostsLiveData;}
    public SearchImagePostsLiveData getSearchImagePostsLiveData() {return searchImagePostsLiveData;}
    public SearchVideoPostsLiveData getSearchVideoPostsLiveData() {return searchVideoPostsLiveData;}
    public SearchGifPostsFragment getSearchGifPostsFragment() {return searchGifPostsFragment;}
}
