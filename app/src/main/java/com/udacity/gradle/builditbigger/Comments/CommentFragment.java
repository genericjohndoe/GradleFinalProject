package com.udacity.gradle.builditbigger.Comments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Comments.CommentsAdapter;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Comment;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentCommentBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class shows comment posts
 */

public class CommentFragment extends Fragment {
    String uid;
    String postId;
    List<Comment> comments;
    CommentsAdapter commentsAdapter;

    public static CommentFragment newInstance(String uid, String pushId) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("post id", pushId);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        postId = getArguments().getString("post id");
        comments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(getActivity(), comments);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCommentBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_comment, container, false);
        bind.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bind.commentsRecyclerView.setAdapter(commentsAdapter);
        bind.commentsRecyclerView.scrollToPosition(comments.size()-1);
        CommentViewModel commentViewModel = ViewModelProviders.of(this, new CommentViewModelFactory(uid, postId)).get(CommentViewModel.class);
        commentViewModel.getCommentLiveData().observe(this, comment -> {
            comments.add(comment);
            commentsAdapter.notifyDataSetChanged();
            bind.commentsRecyclerView.scrollToPosition(comments.size()-1);
        });

        bind.submitImageButton.setOnClickListener(view -> {
                if (bind.commentEditText.getText().toString() != ""){
                    DatabaseReference db = Constants.DATABASE.child("userpostslikescomments/"+uid+"/"+postId+"/comments/commentlist").push();
                    Comment comment = new Comment(Constants.UID, Constants.timeStampString(), Constants.USER.getUserName(), Constants.USER.getUrlString(),
                            bind.commentEditText.getText().toString(),uid,postId,db.getKey());

                    db.setValue(comment);

                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    bind.commentEditText.setText("");
                }
        });
        //todo set up functionality for liking, replying to comments
        return bind.getRoot();
    }
}
