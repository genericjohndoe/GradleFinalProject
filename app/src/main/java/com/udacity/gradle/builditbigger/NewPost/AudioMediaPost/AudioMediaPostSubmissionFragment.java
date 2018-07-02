package com.udacity.gradle.builditbigger.NewPost.AudioMediaPost;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.VideoLifeCyclerObserver;
import com.udacity.gradle.builditbigger.databinding.FragmentAudioMediaPostSubmissionBinding;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioMediaPostSubmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioMediaPostSubmissionFragment extends Fragment {
    private String number;
    private String audioFilePath;
    private Post post;
    private File file;
    private FragmentAudioMediaPostSubmissionBinding bind;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_audio_media_post_submission, container, false);
        getLifecycle().addObserver(new VideoLifeCyclerObserver(getActivity(), bind.simpleexoview));
        if (post != null) {
            Constants.STORAGE.child(post.getJokeTitle()).getFile(file);
            bind.socialEditText.setText(post.getTagline());
        }
        bind.submitbutton.setOnClickListener(view -> {
            if (post != null){
                submitEdittedPost();
            } else {
                createAudioPost();
            }
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
            bind.simpleexoview.getPlayer().prepare(new ExtractorMediaSource(Uri.fromFile(file),
                    dataSourceFactory, extractorsFactory, null, null), false, false);
        }
        bind.simpleexoview.getPlayer().setPlayWhenReady(true);
    }

    private void createAudioPost(){
        String path = "users/" + Constants.UID + "/audio/" + Constants.getCurrentDateAndTime() + ".mp3";
        Constants.STORAGE.child(path).putFile(Uri.fromFile(file))
                .addOnFailureListener(exception -> {
                })
                .addOnSuccessListener(taskSnapshot -> {
                            file.delete();
                            Constants.STORAGE.child(path).getDownloadUrl().addOnSuccessListener(uri ->{
                                String downloadUrl = uri.toString();
                                DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                                String tagline = bind.socialEditText.getText().toString();
                                Post newAudioPost = new Post("", "", System.currentTimeMillis(),
                                        "genre push id", downloadUrl, Constants.UID, db.getKey(), tagline, Constants.VIDEO_AUDIO,
                                        new MetaData("audio", Integer.parseInt(number) + 1, Constants.getTags(tagline)));
                                db.setValue(newAudioPost, ((databaseError, databaseReference) -> {
                                    if (databaseError == null){
                                        getActivity().startActivity(new Intent(getActivity(), HilarityActivity.class));
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
}
