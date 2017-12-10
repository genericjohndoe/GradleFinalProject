package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Joke.Joke;
import com.udacity.gradle.builditbigger.R;

import java.util.Calendar;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewVideoSubmission extends Fragment {
    //todo load from firebase
    private VideoView videoPost;
    private EditText tagline;
    private AutoCompleteTextView genre;
    private String filePath;
    private Button submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getArguments().getString("filepath");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_video_post_submit, container, false);
        videoPost = root.findViewById(R.id.videopost);
        tagline = root.findViewById(R.id.video_tagline);
        genre = root.findViewById(R.id.genre);
        videoPost.setVideoPath(filePath);
        submit = root.findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                String formattedDate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                DatabaseReference db = Constants.DATABASE.child("userposts/" + Constants.UID + "/posts").push();
                Joke newImagePost = new Joke("", Constants.USER.getUserName(), "", formattedDate,
                        genre.getText().toString(), filePath, Constants.UID, db.getKey(), tagline.getText().toString(),Constants.VIDEO);
                db.setValue(newImagePost);
                Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + db.getKey() + "/likes/num").setValue(0);
                Constants.DATABASE.child("userpostslikescomments/" + Constants.UID + "/" + db.getKey() + "/comments/num").setValue(0);
            }
        });
        return root;
    }
}
