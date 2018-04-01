package com.udacity.gradle.builditbigger.Constants;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

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
    public static FirebaseDatabase FIREBASEDATABASE;
    public static FirebaseFirestore FIRESTORE = FirebaseFirestore.getInstance();
    public static DatabaseReference DATABASE;
    public static StorageReference STORAGE = FirebaseStorage.getInstance().getReference();
    public static final int TEXT = 1;
    public static final int IMAGE = 2;
    public static final int VIDEO = 3;
    public static final int GIF = 4;

    public static String timeStampString(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
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
                    Log.i("Hilarity", "tag added " + string);
                }
            }
            return tags;
        }
        return null;
    }
}
