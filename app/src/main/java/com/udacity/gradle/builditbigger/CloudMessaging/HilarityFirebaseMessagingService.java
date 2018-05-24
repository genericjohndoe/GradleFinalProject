package com.udacity.gradle.builditbigger.CloudMessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.udacity.gradle.builditbigger.Comments.CommentActivity;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Messaging.Transcripts.TranscriptActivity;
import com.udacity.gradle.builditbigger.Models.HilarityUser;
import com.udacity.gradle.builditbigger.R;

import java.util.Map;

/**
 * Created by joeljohnson on 1/28/18.
 */

public class HilarityFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("Ladidadi", "onMessageReceived called");
        Map<String, String> data = remoteMessage.getData();
        if (data.get("type").equals("message")) {
            sendNewMessageNotification(data.get("body"), data.get("path"), data.get("title"));
        } else if (data.get("type").equals("mention") || data.get("type").equals("comment")){
            sendNewCommentMentionNotification(data.get("title"), data.get("body"), data.get("uid"),
                    data.get("postid"), Integer.parseInt(data.get("position")));
        }
    }

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(HilarityJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNewMessageNotification(String messageBody, String path, String title) {
        Log.i("Ladidadi", "sendNewMessageNotification");
        Intent intent = new Intent(this, TranscriptActivity.class);
        intent.putExtra("path", path);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_person_black_24dp)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 , notificationBuilder.build());
    }

    private void sendNewCommentMentionNotification(String title, String body, String uid, String pushId, int position){
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("pushId", pushId);
        intent.putExtra("position", position);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }
}
