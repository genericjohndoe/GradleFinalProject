package com.udacity.gradle.builditbigger.Models;

public class Country {
    private String emoji;
    private String twoDigit;

    public Country(String twoDigit, String emoji){
        this.emoji = emoji;
        this.twoDigit = twoDigit;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getTwoDigit() {
        return twoDigit;
    }
}
