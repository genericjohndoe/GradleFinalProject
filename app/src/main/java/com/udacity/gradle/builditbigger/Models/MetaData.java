package com.udacity.gradle.builditbigger.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * MetaData class serves as model for post meta data
 */

public class MetaData implements Parcelable {
    private Map<String, Boolean> keywords;

    public MetaData(){}

    public MetaData(Map<String, Boolean> tags){
        this.keywords = tags;
    }

    public MetaData(Parcel in){
        this.keywords = in.readHashMap(HashMap.class.getClassLoader());
    }

    public Map<String, Boolean> getKeywords() {
        return keywords;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(keywords);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MetaData> CREATOR = new Parcelable.Creator<MetaData>() {
        @Override
        public MetaData createFromParcel(Parcel parcel) {
            return new MetaData(parcel);
        }

        @Override
        public MetaData[] newArray(int i) {
            return new MetaData[i];
        }

    };
}
