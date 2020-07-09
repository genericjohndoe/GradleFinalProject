package com.udacity.gradle.builditbigger.models

data class PostWrapper(val post: Post, val state: Int) {

    companion object{
        const val NEW = 1
        const val EDITTED = 2
        const val REMOVED = 3
    }
}