package com.udacity.gradle.builditbigger.newPost.audioMediaPost;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.FragmentAudioMediaPostSubmissionBinding;
import com.udacity.gradle.builditbigger.interfaces.SetDate;
import com.udacity.gradle.builditbigger.mainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.models.Post;
import com.udacity.gradle.builditbigger.newPost.ScheduledPostDateDialog;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioMediaPostSubmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioMediaPostSubmissionFragment extends Fragment implements SetDate {
    private String number;
    private String audioFilePath;
    private Post post;
    private File file;
    private FragmentAudioMediaPostSubmissionBinding bind;
    private Calendar futureDate = Calendar.getInstance();
    private boolean isConfirmed = false;


    public AudioMediaPostSubmissionFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AudioMediaPostSubmissionFragment.
     */
    public static AudioMediaPostSubmissionFragment newInstance(String number, String audioFilePath) {
        AudioMediaPostSubmissionFragment fragment = new AudioMediaPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putString("number", number);
        args.putString("path", audioFilePath);
        fragment.setArguments(args);
        return fragment;
    }

    public static AudioMediaPostSubmissionFragment newInstance(Post post) {
        AudioMediaPostSubmissionFragment fragment = new AudioMediaPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            number = getArguments().getString("number");
            audioFilePath = getArguments().getString("path");
            if (audioFilePath != null) file = new File(audioFilePath);
            post = getArguments().getParcelable("post");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_audio_media_post_submission, container, false);
        getLifecycle().addObserver(new VideoLifeCyclerObserver(getActivity(), bind.simpleexoview));
        if (post != null) {
            Constants.STORAGE.child(post.getTitle()).getFile(file);
            bind.socialEditText.setText(post.getTagline());
        }
        bind.submitbutton.setOnClickListener(view -> {
            if (post != null){
                submitEdittedPost();
            } else {
                createAudioPost();
            }
        });

        bind.schPostButton.setOnClickListener(view -> {
            ScheduledPostDateDialog.getInstance(this).show(getFragmentManager(),"sd");
        });

        return bind.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), "Hilarity"), bandwidthMeter);
        //SimpleCache cache = new SimpleCache(getActivity().getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024^2*100));
        //CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        if (bind.simpleexoview.getPlayer() != null) {
            ((SimpleExoPlayer) bind.simpleexoview.getPlayer()).prepare(new ProgressiveMediaSource
                    .Factory(dataSourceFactory,extractorsFactory)
                    .createMediaSource(Uri.fromFile(file)));
            bind.simpleexoview.getPlayer().setPlayWhenReady(true);
        }

    }

    private void createAudioPost(){
        DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
        String path = "users/" + Constants.UID + "/audio/" + db.getKey() + ".mp3";
        Constants.STORAGE.child(path).putFile(Uri.fromFile(file))
                .addOnFailureListener(exception -> {
                })
                .addOnSuccessListener(taskSnapshot -> {
                            file.delete();
                            Constants.STORAGE.child(path).getDownloadUrl().addOnSuccessListener(uri ->{
                                String downloadUrl = uri.toString();
                                String tagline = bind.socialEditText.getText().toString();
                                Map<String, Object> keywords = new HashMap<>();
                                keywords.put("audio", true);
                                keywords.put(""+(Integer.parseInt(number) + 1),true);
                                for (String tag : bind.socialEditText.getHashtags()){
                                    keywords.put(tag,true);
                                }
                                long time = (isConfirmed) ? futureDate.getTimeInMillis() : System.currentTimeMillis();
                                Post newAudioPost = new Post("", "", time,
                                        "genre push id", downloadUrl, Constants.UID, db.getKey(), tagline, Constants.VIDEO_AUDIO,
                                         keywords, Constants.INVERSE/time);
                                db.setValue(newAudioPost, ((databaseError, databaseReference) -> {
                                    if (databaseError == null){
                                        startActivity(new Intent(getActivity(), HilarityActivity.class));
                                        Constants.DATABASE.child("userposts/"+Constants.UID+"/num").setValue(Integer.parseInt(number)+1);
                                        Constants.DATABASE.child("userpostslikescomments/"+Constants.UID+"/"+databaseReference.getKey()+"/comments/num").setValue(0);
                                        Constants.DATABASE.child("userpostslikescomments/"+Constants.UID+"/"+databaseReference.getKey()+"/likes/num").setValue(0);
                                    }
                                }));
                            });

                        }
                );
    }

    private void submitEdittedPost(){
        DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts/"+post.getPushId());
        post.setTagline(bind.socialEditText.getText().toString());
        db.setValue(post, (databaseError, databaseReference) -> getActivity().finish());
    }

    @Override
    public void setDate(int year, int month, int day, int hour, int minute) {
        futureDate.set(year, month, day, hour, minute);
    }

    @Override
    public void confirm() {
        isConfirmed = true;
    }
}
