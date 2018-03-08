package com.udacity.gradle.builditbigger.Constants;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

import org.webrtc.PeerConnection;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;


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
    public static void changeFragment(int viewId, Fragment fragment, AppCompatActivity activity){
        activity.getSupportFragmentManager().beginTransaction()
                .replace(viewId, fragment)
                .addToBackStack(null)
                .commit();
    }
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
}
