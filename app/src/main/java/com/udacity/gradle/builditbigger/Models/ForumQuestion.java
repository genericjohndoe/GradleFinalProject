package com.udacity.gradle.builditbigger.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joeljohnson on 3/31/18.
 */

public class ForumQuestion implements Parcelable {

    private String question;
    private HilarityUser hilarityUser;
    private Long timeStamp;
    private String key;

    public ForumQuestion(){}

    public ForumQuestion(String question, HilarityUser hilarityUser, Long timeStamp, String key){
        this.question = question;
        this.hilarityUser = hilarityUser;
        this.timeStamp = timeStamp;
        this.key = key;
    }

    public ForumQuestion(Parcel in){
        question = in.readString();
        hilarityUser = in.readParcelable(HilarityUser.class.getClassLoader());
        timeStamp = in.readLong();
        key = in.readString();
    }

    public HilarityUser getHilarityUser() {
        return hilarityUser;
    }

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
        dest.writeParcelable(hilarityUser,PARCELABLE_WRITE_RETURN_VALUE);
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
