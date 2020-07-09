package com.udacity.gradle.builditbigger.models

data class PostWrapper(val post: Post, val state: Int) {

    companion object{
        val NEW = 1
        val EDITTED = 2
        val REMOVED = 3
    }
}