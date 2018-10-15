package com.udacity.gradle.builditbigger.feed;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostPageKeyedDataSource extends PageKeyedDataSource<String, Post> {

    private Query query = Constants.DATABASE.child("feeds/" + Constants.UID+"/list").orderByKey().limitToLast(30);
    private List<Post> posts = new ArrayList<>();
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            //setValue(new PostWrapper(dataSnapshot.getValue(Post.class),1));
            posts.add(dataSnapshot.getValue(Post.class));
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
            //setValue(new PostWrapper(dataSnapshot.getValue(Post.class),2));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            //setValue(new PostWrapper(dataSnapshot.getValue(Post.class),3));
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };
    private int currentPage;
    private int numPostsInFeed;
    private Post mostRecent = posts.get(posts.size()-1);
    private Post oldestInList = posts.get(0);
    DatabaseReference db = Constants.DATABASE.child("feeds/"+Constants.UID+"/num");



    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<String, Post> callback) {

        query.addChildEventListener(childEventListener);
        currentPage = 0;
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numPostsInFeed = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        int position = numPostsInFeed - 30 * (currentPage + 1);
        callback.onResult(posts, position,30,null,""+currentPage+1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Post> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Post> callback) {

    }
}
