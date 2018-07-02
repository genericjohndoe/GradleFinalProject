package com.udacity.gradle.builditbigger.NewPost.VisualMediaPost;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;
import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentVisualMediaPostSubmissionBinding;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VisualMediaPostSubmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualMediaPostSubmissionFragment extends Fragment {
    private String filePath;
    private String number;
    private Post post;
    private FragmentVisualMediaPostSubmissionBinding bind;
    private String isVideo;
    private int type;
    private File file;

    public VisualMediaPostSubmissionFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment VisualMediaPostSubmissionFragment.
     */
    public static VisualMediaPostSubmissionFragment newInstance(String filePath, String number) {
        VisualMediaPostSubmissionFragment fragment = new VisualMediaPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putString("filepath", filePath);
        args.putString("number", number);
        fragment.setArguments(args);
        return fragment;
    }

    public static VisualMediaPostSubmissionFragment newInstance(Post post) {
        VisualMediaPostSubmissionFragment fragment = new VisualMediaPostSubmissionFragment();
        Bundle args = new Bundle();
        args.putParcelable("post", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filePath = getArguments().getString("filepath");
            if (filePath != null) file = new File(filePath);
            number = getArguments().getString("number");
            post = getArguments().getParcelable("post");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_visual_media_post_submission, container, false);
        Log.i("iefioejwfw", "onCreate");
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

            if (post != null) {
                /*Constants.STORAGE.child(post.getJokeTitle()).getFile(file)
                        .addOnSuccessListener(taskSnapshot -> {
                            try {
                                mmr.setDataSource(getActivity(), Uri.fromFile(file));
                            } catch (Exception e) {

                            }
                        });*/
                bind.socialEditText.setText(post.getTagline());
                Log.i("iefioejwfw", "post isn't null");
                Log.i("iefioejwfw", "post id is " + post.getPushId());
                Log.i("iefioejwfw", "post id is " + post.toString());
            } else {
                Log.i("iefioejwfw", "post is null");
                try {
                    mmr.setDataSource(filePath);
                } catch (Exception e) {

                }
            }
        isVideo = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
        Log.i("wenfhluwhru", "" + (isVideo == null));
        if (isVideo == null) {
            bind.gifImageView.setVisibility(View.VISIBLE);
            bind.simpleExoPlayerView.setVisibility(View.GONE);
            Glide.with(getActivity()).load(file)
                    .into(bind.gifImageView);
            type = Constants.IMAGE_GIF;
        } else {
            bind.gifImageView.setVisibility(View.GONE);
            bind.simpleExoPlayerView.setVisibility(View.VISIBLE);
            type = Constants.VIDEO_AUDIO;
        }
        bind.submitbutton.setOnClickListener(view -> {
            if (post != null) {
                submitEdittedPost();
            } else {
                createNewVisualPost();
            }

        });
        return bind.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVideo != null) {
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "Hilarity"), bandwidthMeter);
            //SimpleCache cache = new SimpleCache(getActivity().getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024^2*100));
            //CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory);
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            if (bind.simpleExoPlayerView.getPlayer() != null) {
                bind.simpleExoPlayerView.getPlayer().prepare(new ExtractorMediaSource(Uri.fromFile(file),
                        dataSourceFactory, extractorsFactory, null, null), false, false);
            }
            bind.simpleExoPlayerView.getPlayer().setPlayWhenReady(true);
        }
    }

    private String addSuffix(String path) {
        int i = path.lastIndexOf('.');
        if (i > 0) {
            return path.substring(i);
        }
        return null;
    }

    private void createNewVisualPost() {
        String path = "users/" + Constants.UID + "/visual/" + Constants.getCurrentDateAndTime() + addSuffix(filePath);
        Constants.STORAGE.child(path).putFile(Uri.fromFile(file))
                .addOnFailureListener(exception -> {
                })
                .addOnSuccessListener(taskSnapshot -> {
                            file.delete();
                            Constants.STORAGE.child(path).getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                                String tagline = bind.socialEditText.getText().toString();
                                Post newAudioPost = new Post(path, "", System.currentTimeMillis(),
                                        "genre push id", downloadUrl, Constants.UID, db.getKey(), tagline, type,
                                        new MetaData("visual", Integer.parseInt(number) + 1, Constants.getTags(tagline)));
                                db.setValue(newAudioPost, ((databaseError, databaseReference) -> {
                                    if (databaseError == null) {
                                        getActivity().startActivity(new Intent(getActivity(), HilarityActivity.class));
                                        Constants.DATABASE.child("userposts/" + Constants.UID + "/num").setValue(Integer.parseInt(number) + 1);
                                        Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + databaseReference.getKey() + "/comments/num").setValue(0);
                                        Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + databaseReference.getKey() + "/likes/num").setValue(0);
                                    }
                                }));
                            });

                        }
                );
    }

    private void submitEdittedPost() {
        Log.i("iefioejwfw", "submitEdittedPost called");
        DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts/" + post.getPushId());
        post.setTagline(bind.socialEditText.getText().toString());
        db.setValue(post, (databaseError, databaseReference) -> getActivity().finish());
    }
}
