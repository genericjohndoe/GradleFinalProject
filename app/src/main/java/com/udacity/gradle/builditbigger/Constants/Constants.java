package com.udacity.gradle.builditbigger.Constants;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.gradle.builditbigger.UserSpecific.HilarityUser;

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
}
