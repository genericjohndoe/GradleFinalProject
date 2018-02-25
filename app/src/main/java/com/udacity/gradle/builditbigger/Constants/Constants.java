package com.udacity.gradle.builditbigger.Constants;


import android.support.v4.app.Fragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

import org.webrtc.PeerConnection;


/**
 * Created by joeljohnson on 10/5/17.
 */

public class Constants {

    public static HilarityUser USER;
    public static String UID;
    public static FirebaseDatabase FIREBASEDATABASE;
    public static DatabaseReference DATABASE;
    public static StorageReference STORAGE = FirebaseStorage.getInstance().getReference();
    public static final int TEXT = 1;
    public static final int IMAGE = 2;
    public static final int VIDEO = 3;
    public static final int GIF = 4;
    public static void changeFragment(int viewId, Fragment fragment) {
        fragment.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(viewId, fragment)
                .addToBackStack(null)
                .commit();
    }
}
