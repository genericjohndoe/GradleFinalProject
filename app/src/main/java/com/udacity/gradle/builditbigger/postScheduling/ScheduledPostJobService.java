package com.udacity.gradle.builditbigger.postScheduling;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.models.Post;

public class ScheduledPostJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        PersistableBundle pb = jobParameters.getExtras();
        String path = pb.getString("path");
        String dbPath = "scheduledposts/" + Constants.UID + "/posts/" + path;
        Constants.DATABASE.child(dbPath)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Constants.DATABASE.child("userposts/"+Constants.UID+"/posts/"+path)
                        .setValue(dataSnapshot.getValue(Post.class), ((databaseError, databaseReference) -> {
                            if (databaseError == null){
                                Constants.DATABASE.child(dbPath).removeValue();
                                jobFinished(jobParameters, false);
                            } else {
                                jobFinished(jobParameters, true);
                                //todo show error message
                            }
                        }));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //only called if job is cancelled
        return false;
    }
}
