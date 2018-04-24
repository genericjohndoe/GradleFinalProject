package com.udacity.gradle.builditbigger.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Genre serves as model for collections of posts
 */

public class Collection implements Parcelable {
    private String title;
    private String author;
    private Boolean restricted;
    private Long timeStamp;
    private String uid;
    private String genreId;

    public Collection() {}

    public Collection(String title, String userName, Boolean restricted,
                  Long time, String uid, String genreId) {
        this.title = title;
        this.author = userName;
        this.restricted = restricted;
        this.timeStamp = time;
        this.uid = uid;
        this.genreId = genreId;
    }

    public Collection(Parcel in){
        title = in.readString();
        author = in.readString();
        timeStamp = in.readLong();
        uid = in.readString();
        genreId = in.readString();
        restricted = in.readByte() != 0;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getUID() {
        return uid;
    }

    public String getGenreId() {
        return genreId;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Collection) && genreId.equals(((Collection) obj).genreId);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(uid);
        dest.writeString(genreId);
        dest.writeByte((byte) (restricted ? 1 : 0));
        dest.writeLong(timeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Collection> CREATOR = new Parcelable.Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel parcel) {return new Collection(parcel);}

        @Override
        public Collection[] newArray(int i) {return new Collection[i];}
    };
}
