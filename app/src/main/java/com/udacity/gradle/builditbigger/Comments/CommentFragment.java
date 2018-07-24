package com.udacity.gradle.builditbigger.Comments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.functions.FirebaseFunctions;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Models.Comment;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentCommentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class shows comment posts
 */

public class CommentFragment extends Fragment {
    private String uid;
    private String postId;
    private int position;
    private List<Comment> comments;
    private CommentsAdapter commentsAdapter;

    public static CommentFragment newInstance(String uid, String pushId) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("post id", pushId);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CommentFragment newInstance(String uid, String pushId, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("post id", pushId);
        bundle.putInt("position", position);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uid = getArguments().getString("uid");
        postId = getArguments().getString("post id");
        position = getArguments().getInt("position");
        comments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(getActivity(), comments);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                if (!bind.commentEditText.getText().toString().equals("")){
                    DatabaseReference db = Constants.DATABASE.child("userpostslikescomments/"+uid+"/"+postId+"/comments/commentlist").push();
                    Comment comment = new Comment(Constants.USER, System.currentTimeMillis(),
                            bind.commentEditText.getText().toString(),uid,postId,db.getKey());

                    //todo change to transaction
                    db.setValue(comment, (databaseError, databaseReference) -> {
                        if (databaseError == null) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("userNameList", bind.commentEditText.getMentions());
                            data.put("commentFragmentUid", uid);
                            data.put("commentFragmentPostId", postId);
                            data.put("position", comments.size()-1);
                            data.put("commentorUserName", comment.getHilarityUser().getUserName());
                            data.put("commentContent", comment.getCommentContent());
                            //how to get access to list of mentions on server side
                            FirebaseFunctions.getInstance().getHttpsCallable("onCommentMentionCreated")
                                    .call(data);
                            FirebaseFunctions.getInstance().getHttpsCallable("onCommentCreated")
                                    .call(data);
                        }
                    });

                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    bind.commentEditText.setText("");
                }
        });
        if (position > -1) bind.commentsRecyclerView.scrollToPosition(position);
        return bind.getRoot();
    }
}
