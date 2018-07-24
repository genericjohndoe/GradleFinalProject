package com.udacity.gradle.builditbigger.Constants;


import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * provides a number of methods/constants/variables used in a number of other classes
 */

public class Constants {

    public static HilarityUser USER;
    public static String UID;
    public static String TAGLINE = "tagline";
    public static FirebaseDatabase FIREBASEDATABASE;
    public static FirebaseFirestore FIRESTORE = FirebaseFirestore.getInstance();
    public static DatabaseReference DATABASE;
    public static StorageReference STORAGE = FirebaseStorage.getInstance().getReference();
    public static final int TEXT = 1;
    public static final int IMAGE_GIF = 2;
    public static final int VIDEO_AUDIO = 3;

    public static String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return df.format(c.getTime());
    }

    public static CharSequence formattedTimeString(Context context, long timeInMillis){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), context.getResources().getConfiguration().locale);
        cal.setTimeInMillis(timeInMillis);
        return android.text.format.DateFormat.format("d MMM yyyy HH:mm",cal);
    }

    public static Map<String, Boolean> getTags(String tagline) {
        if (!(tagline.equals(""))) {
            String[] array = tagline.split(" ,;:.!?");
            Map<String, Boolean> tags = new HashMap<>();
            for (String string : array) {
                if (string.substring(0, 1).equals("#")) {
                    tags.put(string.substring(1), true);
                }
            }
            return tags;
        }
        return null;
    }

    public static String getExtension(String filepath){
        String filenameArray[] = filepath.split("\\.");
        return filenameArray[filenameArray.length-1];
    }

    public static boolean isImage(String filepath){
        String[] imageFileTypes = new String[]{"bmp", "jpg", "jpeg", "png", "webp"};
        for (String type: imageFileTypes){
            if (type.contains(getExtension(filepath))) return true;
        }
        return false;
    }

    public static int STATE_ADDED = 1;
    public static int STATE_CHANGED = 2;
    public static int STATE_REMOVED = 3;
}
