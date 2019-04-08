package com.udacity.gradle.builditbigger.newPost.documentPost;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.interfaces.SetDate;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewTextPostSubmissionBinding;
import com.udacity.gradle.builditbigger.newPost.ScheduledPostDateDialog;
import com.udacity.gradle.builditbigger.postScheduling.ScheduledPostJobService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTextPostSubmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTextPostSubmissionFragment extends Fragment implements SetDate {
    private static final String TITLE = "title";
    private static final String BODY = "body";
    public static final String TAGLINE = "tagline";
    public static final String NUMBER = "number";
    public static final String SYNOPSIS = "synopsis";

    private String title;
    private String body;
    private String tagline;
    private String number;
    private String synopsis;
    private Post post;
    private Calendar futureDate = Calendar.getInstance();
    private boolean isConfirmed = false;
    private int numScheduledposts;

    public NewTextPostSubmissionFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewTextPostSubmissionFragment.
     */
    public static NewTextPostSubmissionFragment newInstance(String title, String body, String tagline, String number, String synopsis) {
        NewTextPostSubmissionFragment fragment = new NewTextPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(BODY, body);
        args.putString(TAGLINE, tagline);
        args.putString(NUMBER, number);
        args.putString(SYNOPSIS, synopsis);
        fragment.setArguments(args);
        return fragment;
    }

    public static NewTextPostSubmissionFragment newInstance(Post post) {
        NewTextPostSubmissionFragment fragment = new NewTextPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            body = getArguments().getString(BODY);
            tagline = getArguments().getString(TAGLINE);
            number = getArguments().getString(NUMBER);
            synopsis = getArguments().getString(SYNOPSIS);
            post = getArguments().getParcelable("post");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewTextPostSubmissionBinding bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_new_text_post_submission, container, false);
        if (post != null) {
            bind.titleTextView.setText(post.getTitle());
            bind.bodyTextView.setText(Html.fromHtml(post.getBody()));
            bind.socialTextView.setText(post.getTagline());
            bind.synopsisTextView.setText(post.getSynopsis());
        } else {
            bind.titleTextView.setText(title);
            if (body != null) bind.bodyTextView.setText(Html.fromHtml(body));
            bind.socialTextView.setText(tagline);
            bind.synopsisTextView.setText(synopsis);
        }
        bind.bodyTextView.setMovementMethod(new ScrollingMovementMethod());

        bind.scheduleButton.setOnClickListener(view -> ScheduledPostDateDialog.getInstance(this).show(getFragmentManager(),"sd"));

        bind.submitButton.setOnClickListener(view -> {
            DatabaseReference db;
            if (post != null) {
                db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts/" + post.getPushId());
                db.setValue(post);
                startActivity(new Intent(getActivity(), HilarityActivity.class));
                getActivity().finish();
            } else {
                Map<String, Object> keywords = new HashMap<>();
                keywords.put("text", true);
                keywords.put("document", true);
                keywords.put(""+(Integer.parseInt(number) + 1), true);
                for (String tag: bind.socialTextView.getHashtags()){
                    keywords.put(tag,true);
                }
                long time = (isConfirmed) ? futureDate.getTimeInMillis() : System.currentTimeMillis();
                Post newJoke = new Post(title, (body != null) ? body : "", time,
                        synopsis, "", Constants.UID, null, tagline, Constants.TEXT,
                        keywords, Constants.INVERSE/time);
                if (!isConfirmed) {
                    db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                    newJoke.setPushId(db.getKey());
                    db.setValue(newJoke, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            startActivity(new Intent(getActivity(), HilarityActivity.class));
                            Constants.DATABASE.child("userposts/" + Constants.UID + "/num").setValue(Integer.parseInt(number) + 1);
                            Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + databaseReference.getKey() + "/comments/num").setValue(0);
                            Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + databaseReference.getKey() + "/likes/num").setValue(0);
                        }
                    });
                } else {
                    db = Constants.DATABASE.child("scheduledposts/" + Constants.UID + "/posts").push();
                    newJoke.setPushId(db.getKey());
                    db.setValue(newJoke, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            Constants.DATABASE.child("scheduledposts/" + Constants.UID + "/num").setValue(numScheduledposts+1);
                            PersistableBundle pb = new PersistableBundle();
                            pb.putString("path", newJoke.getPushId());
                            ComponentName componentName = new ComponentName(getActivity(), ScheduledPostJobService.class);
                            JobInfo jobInfo = new JobInfo.Builder(numScheduledposts+1, componentName)
                                    .setMinimumLatency(time - System.currentTimeMillis())
                                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                    .setExtras(pb)
                                    .setPersisted(true)
                                    .build();
                            JobScheduler jobScheduler = (JobScheduler)getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
                            int resultCode = jobScheduler.schedule(jobInfo);
                            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                                Log.d("hi", "Job scheduled!");
                            } else {
                                Log.d("hi", "Job not scheduled");
                            }
                        }
                    });
                }
            }
        });
        bind.editButton.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        bind.discardButton.setOnClickListener(view -> {
            getActivity().startActivity(new Intent(getActivity(), HilarityActivity.class));
        });
        return bind.getRoot();
    }

    @Override
    public void setDate(int year, int month, int day, int hour, int minute) {
        futureDate.set(year, month, day, hour, minute);
    }

    @Override
    public void confirm() {
        isConfirmed = true;
        Log.i("timeset", "time confirmed");
        Constants.DATABASE.child("scheduledposts/" + Constants.UID + "/num").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer num = dataSnapshot.getValue(Integer.class);
                numScheduledposts = (num != null) ? num : 0;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }


}
