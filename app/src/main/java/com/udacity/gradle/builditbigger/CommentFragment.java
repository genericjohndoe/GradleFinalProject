package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.udacity.gradle.builditbigger.Constants.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by joeljohnson on 12/13/17.
 */

public class CommentFragment extends Fragment {
    //todo get comment to show without clicking on the edittext
    RecyclerView recyclerView;
    EditText commentEditText;
    ImageButton submitImageButton;

    String uid;
    String postId;
    List<Comment> comments;
    CommentsAdapter commentsAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        postId = getArguments().getString("post id");
        comments = new ArrayList<>();
        Constants.DATABASE.child("userpostslikescomments/"+uid+"/"+postId+"/list")
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                comments.add(comment);
                commentsAdapter.notifyDataSetChanged();
                if (recyclerView != null) recyclerView.scrollToPosition(comments.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        commentsAdapter = new CommentsAdapter(getActivity(), comments);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_comment, container, false);
        recyclerView = root.findViewById(R.id.comments_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(commentsAdapter);
        recyclerView.scrollToPosition(comments.size()-1);
        commentEditText = root.findViewById(R.id.comment_editText);
        submitImageButton = root.findViewById(R.id.submit_imageButton);
        submitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentEditText.getText().toString() != ""){
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(System.currentTimeMillis());
                    String formattedDate = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
                    Comment comment = new Comment(Constants.UID, formattedDate, Constants.USER.getUserName(), Constants.USER.getUrlString(),
                            commentEditText.getText().toString());
                    Constants.DATABASE.child("userpostslikescomments/"+uid+"/"+postId+"/list").push().setValue(comment);
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    commentEditText.setText("");
                }
            }
        });
        //todo set up functionality for liking, replying to comments
        return root;
    }
}
