/*
package com.udacity.gradle.builditbigger.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Joke class serves as model for user generated content
 *//*


public class Post implements Parcelable {
    private String title;
    private String body;
    private String synopsis;
    private Long timeStamp;
    private String uid;
    private String mediaURL;
    private String pushId;
    private String tagline;
    private Integer type;
    private List<String> taglist;
    private Map<String,Object> metaData;
    private Double inverseTimeStamp;


    public Post() {}

    public Post(String jokeTitle, String body, Long time,
                String synopsis, String url, String uid,
                String pushId, String tagline, Integer type,
                Map<String, Object> metaData, Double inverseTimeStamp) {
        this.title = jokeTitle;
        this.body = body;
        timeStamp = time;
        this.synopsis = synopsis;
        this.uid = uid;
        mediaURL = url;
        this.pushId = pushId;
        this.tagline = tagline;
        this.type = type;
        taglist = new ArrayList<>();
        this.metaData = metaData;
        this.inverseTimeStamp = inverseTimeStamp;
    }

    public Post(Parcel in){
        title = in.readString();
        body = in.readString();
        uid = in.readString();
        tagline = in.readString();
        type = in.readInt();
        pushId = in.readString();
        timeStamp = in.readLong();
        mediaURL = in.readString();
        metaData = in.readHashMap(HashMap.class.getClassLoader());
        inverseTimeStamp = in.readDouble();
        synopsis = in.readString();
        taglist = in.readArrayList(ArrayList.class.getClassLoader());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String text) {
        title = text;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
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

    public Map<String, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, Object> metaData){
        this.metaData = metaData;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Double getInverseTimeStamp() {
        return inverseTimeStamp;
    }

    @Override
    public boolean equals(Object object){
        return (object instanceof Post) && pushId.equals(((Post) object).getPushId());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(uid);
        dest.writeString(tagline);
        dest.writeInt(type);
        dest.writeString(pushId);
        dest.writeLong(timeStamp);
        dest.writeString(mediaURL);
        dest.writeMap(metaData);
        dest.writeDouble(inverseTimeStamp);
        dest.writeString(synopsis);
        dest.writeList(taglist);
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
*/
