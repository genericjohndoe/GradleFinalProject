package com.udacity.gradle.builditbigger.NewPost.ImagePost;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.MainUI.HilarityActivity;

import com.udacity.gradle.builditbigger.Models.MetaData;
import com.udacity.gradle.builditbigger.Models.Post;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewImagePostSubmissionBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewImageSubmission extends Fragment {
    private FragmentNewImagePostSubmissionBinding bind;
    private File file;
    private String number;
    private Post post;

    public NewImageSubmission() {}

    public static NewImageSubmission newInstance(String filepath, String number) {
        NewImageSubmission fragment = new NewImageSubmission();
        fragment.file = new File(filepath);
        fragment.number = number;
        return fragment;
    }

    public static NewImageSubmission newInstance(Post post) {
        NewImageSubmission fragment = new NewImageSubmission();
        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) post = getArguments().getParcelable("post");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_new_image_post_submission, container, false);
        if (post != null){
            Glide.with(this).load(post.getMediaURL()).into(bind.imagePost);
            bind.imageTagline.setText(post.getTagline());
        } else {
            Glide.with(this).load(file).into(bind.imagePost);
        }
        bind.submitButton.setOnClickListener(view -> {
                    if (post != null){
                        submitEdittedPost();
                    } else {
                        newPost();
                    }
        });
        return bind.getRoot();
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void newPost(){
        Constants.STORAGE.child("users/" + Constants.UID + "/images/" + getCurrentDateAndTime() + ".png").putFile(Uri.fromFile(file))
                .addOnFailureListener(exception -> {
                    Log.i("cloud storage exception", exception.toString());
                })
                .addOnSuccessListener((taskSnapshot) -> {
                    file.delete();
                    String downloadUrl = taskSnapshot.getUploadSessionUri().toString();
                    DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                    Post newImagePost = new Post("", "", System.currentTimeMillis(),
                            "genre push id", downloadUrl, Constants.UID,
                            db.getKey(), bind.imageTagline.getText().toString(), Constants.IMAGE_GIF,
                            new MetaData("image", Integer.parseInt(number)+1,Constants.getTags(bind.imageTagline.getText().toString())));
                    db.setValue(newImagePost, ((databaseError, databaseReference) -> {
                        if (databaseError == null){
                            getActivity().startActivity(new Intent(getActivity(), HilarityActivity.class));
                            Constants.DATABASE.child("userposts/"+Constants.UID+"/num").setValue(Integer.parseInt(number)+1);
                            Constants.DATABASE.child("userpostslikescomments/"+Constants.UID+"/"+databaseReference.getKey()+"/comments/num").setValue(0);
                            Constants.DATABASE.child("userpostslikescomments/"+Constants.UID+"/"+databaseReference.getKey()+"/likes/num").setValue(0);
                        }
                    }));
                });
    }

    private void submitEdittedPost(){

    }
}
