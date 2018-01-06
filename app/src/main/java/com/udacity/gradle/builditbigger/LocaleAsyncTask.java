package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

/**
 * Created by joeljohnson on 11/28/17.
 */

public class LocaleAsyncTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... obj) {
        Log.i("async", "DIB");
        Locale[] locales = Locale.getAvailableLocales();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            for (Locale locale : locales) {
                Log.i("async " + locale.getDisplayLanguage(), locale.getDisplayLanguage(locale));
                FirebaseDatabase.getInstance().getReference("languages").child(locale.getDisplayLanguage()).setValue(locale.getDisplayLanguage(locale));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            }
        } else {
            Log.i("async", "no auth");
        }
        return null;
    }
}
