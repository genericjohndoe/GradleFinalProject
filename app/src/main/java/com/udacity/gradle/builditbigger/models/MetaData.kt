package com.udacity.gradle.builditbigger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MetaData(var keywords: Map<String, Boolean>) : Parcelable