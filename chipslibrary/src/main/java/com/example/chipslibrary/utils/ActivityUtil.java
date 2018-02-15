package com.example.chipslibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * Created by joeljohnson on 2/1/18.
 */

public class ActivityUtil {
    public static Activity scanForActivity(Context context) {
        if (context == null)
            return null;
        else if (context instanceof Activity)
            return (Activity)context;
        else if (context instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper)context).getBaseContext());

        return null;
    }
}
