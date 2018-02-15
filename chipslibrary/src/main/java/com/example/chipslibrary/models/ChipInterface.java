package com.example.chipslibrary.models;

import android.graphics.drawable.Drawable;
import android.net.Uri;

/**
 * Created by joeljohnson on 2/1/18.
 */

public interface ChipInterface {
    Object getId();
    Uri getAvatarUri();
    Drawable getAvatarDrawable();
    String getLabel();
    String getInfo();
}
