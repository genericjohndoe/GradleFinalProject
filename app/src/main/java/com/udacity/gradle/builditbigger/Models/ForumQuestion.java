package com.udacity.gradle.builditbigger.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumQuestion implements Parcelable {

    private String question;
    private String hilarityUserUID;
    private Long timeStamp;
    private String key;

    public ForumQuestion(){}

    public ForumQuestion(String question, String hilarityUserUID, Long timeStamp, String key){
        this.question = question;
        this.hilarityUserUID = hilarityUserUID;
        this.timeStamp = timeStamp;
        this.key = key;
    }

    public ForumQuestion(Parcel in){
        question = in.readString();
        hilarityUserUID = in.readString();
        timeStamp = in.readLong();
        key = in.readString();
    }

    public String getHilarityUserUID() {return hilarityUserUID;}

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getQuestion() {
        return question;
    }
    public String getKey() {return key;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(hilarityUserUID);
        dest.writeLong(timeStamp);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ForumQuestion> CREATOR = new Parcelable.Creator<ForumQuestion>() {
        @Override
        public ForumQuestion createFromParcel(Parcel parcel) {
            return new ForumQuestion(parcel);
        }

        @Override
        public ForumQuestion[] newArray(int i) {
            return new ForumQuestion[i];
        }

    };

    @Override
    public boolean equals(Object obj) {
        return ((obj instanceof ForumQuestion) && ((ForumQuestion) obj).getKey().equals(key));
    }
}
