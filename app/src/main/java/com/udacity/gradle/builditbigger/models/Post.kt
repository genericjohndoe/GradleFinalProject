package com.udacity.gradle.builditbigger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(var title: String, var body: String, val timeStamp: Long, var synopsis: String,
                var mediaUrl: String, val uid: String, val pushId: String, var tagline: String,
                val type: Int, var metaData: Map<String, Boolean>, val inverseTimeStamp: Double) :
                Parcelable {

    override fun equals(other: Any?): Boolean {
        return (other is Post) && pushId == other.pushId
    }

}