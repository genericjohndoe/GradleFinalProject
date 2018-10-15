package com.udacity.gradle.builditbigger.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * HilarityUser class serves as model for user
 */
@Entity(tableName = "hilarityusers")
public class HilarityUser implements Parcelable {
    private String userName;
    private String urlString;
    @PrimaryKey @NonNull private String uid;

    public HilarityUser() {}

    @Ignore
    public HilarityUser(String userName, String urlString, String uid) {
        this.userName = userName;
        this.urlString = urlString;
        this.uid = uid;
    }

    public HilarityUser(Parcel in){
        userName = in.readString();
        urlString = in.readString();
        uid = in.readString();
    }

    public String getUserName() {
        return userName;
    }

    public String getUrlString() {
        return urlString;
    }

    public String getUid() {return uid;}

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public void setUid(String uid) {this.uid = uid;}

    @Override
    public boolean equals(Object obj) {
            return (obj instanceof HilarityUser) && uid.equals(((HilarityUser) obj).getUid());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(urlString);
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<HilarityUser> CREATOR = new Parcelable.Creator<HilarityUser>() {
        @Override
        public HilarityUser createFromParcel(Parcel parcel) {
            return new HilarityUser(parcel);
        }

        @Override
        public HilarityUser[] newArray(int i) {
            return new HilarityUser[i];
        }

    };
}

