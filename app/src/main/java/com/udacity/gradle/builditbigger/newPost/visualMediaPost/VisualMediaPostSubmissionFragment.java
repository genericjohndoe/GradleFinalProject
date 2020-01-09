package com.udacity.gradle.builditbigger.newPost.visualMediaPost;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.FragmentVisualMediaPostSubmissionBinding;
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
 * Use the {@link VisualMediaPostSubmissionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VisualMediaPostSubmissionFragment extends Fragment implements SetDate {
    private String filePath;
    private String number;
    private Post post;
    private FragmentVisualMediaPostSubmissionBinding bind;
    private String isVideo;
    private int type;
    private File file;
    private Calendar futureDate = Calendar.getInstance();
    private boolean isConfirmed = false;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bind = DataBindingUtil.inflate(inflater,
                R.layout.fragment_visual_media_post_submission, container, false);

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
        } else {
            try {
                mmr.setDataSource(filePath);
            } catch (Exception e) {

            }
        }
        isVideo = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
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

        bind.scheduleButton.setOnClickListener(view -> ScheduledPostDateDialog.getInstance(this).show(getFragmentManager(),"sd"));
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
            //Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            if (bind.simpleExoPlayerView.getPlayer() != null) {
                ((SimpleExoPlayer) bind.simpleExoPlayerView.getPlayer()).prepare(new ProgressiveMediaSource
                        .Factory(dataSourceFactory,extractorsFactory)
                        .createMediaSource(Uri.fromFile(file))
                );
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
        DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
        String path = "users/" + Constants.UID + "/visual/" + db.getKey() + addSuffix(filePath);
        Constants.STORAGE.child(path).putFile(Uri.fromFile(file))
                .addOnFailureListener(exception -> {
                })
                .addOnSuccessListener(taskSnapshot -> {
                            file.delete();
                            Constants.STORAGE.child(path).getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                String tagline = bind.socialEditText.getText().toString();
                                Map<String, Object> keywords = new HashMap<>();
                                if (isVideo != null) keywords.put("video", true);
                                if ((isVideo == null) && addSuffix(filePath).equals(".gif"))
                                    keywords.put("gif", true);
                                if ((isVideo == null) && !addSuffix(filePath).equals(".gif"))
                                    keywords.put("image", true);
                                keywords.put("visual", true);
                                keywords.put("" + (Integer.parseInt(number) + 1), true);
                                for (String tag : bind.socialEditText.getHashtags()) {
                                    keywords.put(tag, true);
                                }
                                long time = (isConfirmed) ? futureDate.getTimeInMillis() : System.currentTimeMillis();
                                Post newAudioPost = new Post(path, "", time,
                                        "genre push id", downloadUrl, Constants.UID, db.getKey(), tagline, type,
                                        keywords, Constants.INVERSE / time);
                                db.setValue(newAudioPost, ((databaseError, databaseReference) -> {
                                    if (databaseError == null) {
                                        startActivity(new Intent(getActivity(), HilarityActivity.class));
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

    @Override
    public void setDate(int year, int month, int day, int hour, int minute) {
        futureDate.set(year, month, day, hour, minute);
    }

    @Override
    public void confirm() {
        isConfirmed = true;
    }
}
