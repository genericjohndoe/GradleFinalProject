package com.udacity.gradle.builditbigger.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Joke class serves as model for user generated content
 */

public class Post implements Parcelable {
    private String jokeTitle;
    private String jokeBody;
    private Long timeStamp;
    private String genre;
    private String uid;
    private String mediaURL;
    private String pushId;
    private String tagline;
    private Integer type;
    private List<String> taglist;
    private MetaData metaData;


    public Post() {}

    public Post(String jokeTitle, String body, Long time, String genre,
                String url, String uid, String pushId, String tagline, Integer type, MetaData metaData) {
        this.jokeTitle = jokeTitle;
        jokeBody = body;
        timeStamp = time;
        this.genre = genre;
        this.uid = uid;
        mediaURL = url;
        this.pushId = pushId;
        this.tagline = tagline;
        this.type = type;
        taglist = new ArrayList<>();
        this.metaData = metaData;
    }

    public Post(Parcel in){
        jokeBody = in.readString();
        jokeTitle = in.readString();
        uid = in.readString();
        tagline = in.readString();
        type = in.readInt();
        pushId = in.readString();
        timeStamp = in.readLong();
        mediaURL = in.readString();
        metaData = in.readParcelable(MetaData.class.getClassLoader());
    }

    public String getJokeTitle() {
        return jokeTitle;
    }

    public void setJokeTitle(String text) {
        jokeTitle = text;
    }

    public String getJokeBody() {
        return jokeBody;
    }

    public void setJokeBody(String body) {
        jokeBody = body;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getGenre() {
        return genre;
    }

    public String getUID() {
        return uid;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public String getPushId() {
        return pushId;
    }

    public String getTagline() {
        return tagline;
    }

    public Integer getType(){ return type;}

    public List<String> getTaglist(){
        return taglist;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData){
        this.metaData = metaData;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    @Override
    public boolean equals(Object object){
        return (object instanceof Post) && pushId.equals(((Post) object).getPushId());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jokeTitle);
        dest.writeString(jokeBody);
        dest.writeString(uid);
        dest.writeString(tagline);
        dest.writeInt(type);
        dest.writeString(pushId);
        dest.writeLong(timeStamp);
        dest.writeString(mediaURL);
        dest.writeParcelable(metaData,PARCELABLE_WRITE_RETURN_VALUE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel parcel) {return new Post(parcel);}

        @Override
        public Post[] newArray(int i) {return new Post[i];}
    };
}
