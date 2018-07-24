package com.udacity.gradle.builditbigger.CloudMessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.udacity.gradle.builditbigger.Comments.CommentActivity;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Forums.Replies.ForumQuestionActivity;
import com.udacity.gradle.builditbigger.Messaging.Transcripts.TranscriptActivity;
import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        Map<String, String> data = remoteMessage.getData();
        if (data != null && data.get(getString(R.string.type)).equals(getString(R.string.message))) {
            sendNewMessageNotification(data.get(getString(R.string.body)), data.get(getString(R.string.path)), data.get(getString(R.string.title)));
        } else if (data.get(getString(R.string.type)).equals(getString(R.string.mentions_lc)) || data.get(getString(R.string.type)).equals(getString(R.string.comment))){
            sendNewCommentMentionNotification(data.get(getString(R.string.title)), data.get(getString(R.string.body)), data.get(getString(R.string.uid)),
                    data.get("postid"), Integer.parseInt(data.get(getString(R.string.position))));
        } else if (data.get(getString(R.string.type)).equals(getString(R.string.forums_lc))) {
            Constants.DATABASE.child("").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sendForumNotification(dataSnapshot.getValue(ForumQuestion.class),
                            data.get(getString(R.string.title)), data.get(getString(R.string.body)));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
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
        Intent intent = new Intent(this, TranscriptActivity.class);
        intent.putExtra(getString(R.string.path), path);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "new_message_channel";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_person_black_24dp)
                        .setLargeIcon(getBitmapfromUrl("https://ibin.co/2t1lLdpfS06F.png"))//user for profile pic
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(getBitmapfromUrl("https://ibin.co/2t1lLdpfS06F.png")))//use of picture was sent
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            final String KEY_TEXT_REPLY = "key_text_reply";


            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel(getString(R.string.reply))
                    .build();

            Intent intent1 = new Intent(this, NotifcationMessageReceiver.class);
            intent1.putExtra(getString(R.string.path), path);

            PendingIntent replyPendingIntent =
                    PendingIntent.getBroadcast(getApplicationContext(),
                            100,
                            intent1,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.baseline_send_24px,
                            getString(R.string.reply), replyPendingIntent)
                            .addRemoteInput(remoteInput)
                            .build();

            notificationBuilder.addAction(action);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(2 , notificationBuilder.build());
    }

    private void sendNewCommentMentionNotification(String title, String body, String uid, String pushId, int position){
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra(getString(R.string.uid), uid);
        intent.putExtra(getString(R.string.pushId), pushId);
        intent.putExtra(getString(R.string.position), position);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "new_comment_channel";
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

    private void sendForumNotification(ForumQuestion forumQuestion, String title, String body){
        Intent intent = new Intent(this, ForumQuestionActivity.class);
        intent.putExtra("question", forumQuestion);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "new_forum_channel";
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

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
