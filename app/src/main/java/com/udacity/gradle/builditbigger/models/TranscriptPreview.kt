package com.udacity.gradle.builditbigger.models

data class TranscriptPreview(val message: Message, var conversationalists: List<HilarityUser>,
            val path: String, val original: Boolean, val hasUnreadMessages: Boolean){

    override fun equals(other: Any?): Boolean {
        return (other is TranscriptPreview) && path == other.path
    }

    fun compare(transcriptPreview: TranscriptPreview): Boolean {
        return message.equals(transcriptPreview.message)
    }

}