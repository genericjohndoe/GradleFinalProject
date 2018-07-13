package com.udacity.gradle.builditbigger.Explore;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Post;

import javax.annotation.Nullable;

/**
 * retrieves all posts
 */

public class ExploreLiveData extends LiveData<Post> {
    private CollectionReference collectionReference;
    private Context context;

    public ExploreLiveData(Context context){
        collectionReference = Constants.FIRESTORE.collection("posts");
        this.context = context;
    }

    private EventListener<QuerySnapshot> eventListener = (queryDocumentSnapshots, e) -> {
        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()){
            setValue(snap.toObject(Post.class));
        }
        };

    @Override
    protected void onActive() {
        super.onActive();
        collectionReference.addSnapshotListener((Activity) context, eventListener);
    }
}
