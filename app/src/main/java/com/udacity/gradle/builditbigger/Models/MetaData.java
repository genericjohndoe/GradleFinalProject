package com.udacity.gradle.builditbigger.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * MetaData class serves as model for post meta data
 */

public class MetaData implements Parcelable {
    private String type;
    private int number;
    private Map<String, Boolean> tags;

    public MetaData(){}

    public MetaData(String type, int number, Map<String, Boolean> tags){
        this.type = type;
        this.number = number;
        this.tags = tags;
    }

    public MetaData(Parcel in){
        this.type = in.readString();
        this.number = in.readInt();
        this.tags = in.readHashMap(HashMap.class.getClassLoader());
    }

    public int getNumber() {
        return number;
    }

    public Map<String, Boolean> getTags() {
        return tags;
    }

    public String getType() {
        return type;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(number);
        dest.writeMap(tags);
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
