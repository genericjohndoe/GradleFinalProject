package com.udacity.gradle.builditbigger.models

/**
 * A class that represents message sent between users
 *
 * @property hilarityUser, the name of the user sending the message
 * @property contents, the list of messages set between users
 * @property timeStamp, when the message was sent
 * @property pushId, the message's id, generated by firebase
 * @property original, boolean used in cloud function during processing of message
 */

data class Message(var hilarityUser: String, val contents: List<String>, val timeStamp: Long,
                   val pushId: String, var original: Boolean) {

    override fun equals(other: Any?): Boolean {
        return (other is Message) && pushId == other.pushId
    }
}