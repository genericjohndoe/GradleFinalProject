package com.udacity.gradle.builditbigger.explore;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.Post;

/**
 * retrieves all posts
 */

public class ExploreLiveData extends LiveData<Post> {
    private CollectionReference collectionReference;
    private ListenerRegistration registration;

    public ExploreLiveData(){
        collectionReference = Constants.FIRESTORE.collection("posts");
    }

    private EventListener<QuerySnapshot> eventListener = (queryDocumentSnapshots, e) -> {
        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()){
            Log.i("hilarityApp", snap.toObject(Post.class).toString());
            setValue(snap.toObject(Post.class));
        }
        };

    @Override
    protected void onActive() {
        super.onActive();
        registration = collectionReference.addSnapshotListener(eventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        registration.remove();
    }
}
