package com.udacity.gradle.builditbigger.Jokes;

import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDataSource extends ItemKeyedDataSource<String, Post> {

    private Query query;
    private ValueEventListener valueEventListener;
    private Double startAt;

    private List<Post> posts = new ArrayList<>();

    public PostDataSource(String path){
        query = Constants.DATABASE.child(path).orderByChild("inverseTimeStamp").limitToFirst(20);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Post> callback) {
        query(true, callback);
        Log.i("paging", "loadinit called");
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {
        query(false, callback);
        Log.i("paging", "loadAfter called");
    }

    @NonNull
    @Override
    public String getKey(@NonNull Post item) {return item.getPushId();}

    public void query(boolean initialLoad, LoadCallback<Post> callback){
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index = (int) dataSnapshot.getChildrenCount();
                if (posts.size() % 20 == 0) {
                    int count = 1;
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        posts.add(snap.getValue(Post.class));
                        count += 1;
                        if (count == index)
                            startAt = snap.getValue(Post.class).getInverseTimeStamp();
                    }
                    callback.onResult(posts);
                    query.removeEventListener(valueEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        if (!initialLoad) query.startAt(startAt);
        query.addValueEventListener(valueEventListener);
    }
}
