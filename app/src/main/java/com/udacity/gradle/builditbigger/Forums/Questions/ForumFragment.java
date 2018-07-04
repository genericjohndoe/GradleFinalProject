package com.udacity.gradle.builditbigger.Forums.Questions;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.udacity.gradle.builditbigger.Forums.CreateQuestion.NewQuestionActivity;
import com.udacity.gradle.builditbigger.Forums.ForumQuestionsViewModel;
import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentForumBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment {

    public ForumFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ForumFragment.
     */
    public static ForumFragment newInstance() {
        return new ForumFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentForumBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_forum, container, false);
        List<ForumQuestion> forumQuestions = new ArrayList<>();
        ForumQuestionAdapter forumQuestionAdapter = new ForumQuestionAdapter(forumQuestions, getActivity());
        bind.questionsRecyclerview.setAdapter(forumQuestionAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        llm.setStackFromEnd(true);
        bind.questionsRecyclerview.setLayoutManager(llm);
        ViewModelProviders.of(this).get(ForumQuestionsViewModel.class).getForumQuestionsLiveData().observe(this, question -> {
            if (!forumQuestions.contains(question)) {
                forumQuestions.add(question);
                forumQuestionAdapter.notifyDataSetChanged();
            }
        });
        bind.fab.setOnClickListener(view ->{
            startActivity(new Intent(getActivity(), NewQuestionActivity.class));
        });
        return bind.getRoot();
    }

}
