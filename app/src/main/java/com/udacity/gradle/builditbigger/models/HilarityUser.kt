package com.udacity.gradle.builditbigger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A class that models the users
 *
 * @property userName, the user name for the user, editable
 * @property urlString,  the url of the image useed for the profile image
 * @property uid, the user's id, generated by firebase
 */

@Parcelize
data class HilarityUser(var userName: String, var urlString: String, val uid: String) : Parcelable {

    override fun equals(other: Any?): Boolean {
        return (other is HilarityUser) && uid == other.uid
    }

}