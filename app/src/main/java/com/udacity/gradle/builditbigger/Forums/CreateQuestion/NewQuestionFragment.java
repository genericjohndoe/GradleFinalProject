package com.udacity.gradle.builditbigger.Forums.CreateQuestion;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Forums.Replies.ForumQuestionActivity;
import com.udacity.gradle.builditbigger.Models.ForumQuestion;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentNewQuestionBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewQuestionFragment extends Fragment {

    public NewQuestionFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewQuestionFragment.
     */
    public static NewQuestionFragment newInstance() {
        NewQuestionFragment fragment = new NewQuestionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewQuestionBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_new_question, container, false);
        bind.submitButton.setOnClickListener(view ->{
            String question = bind.questionEditText.getText().toString();
            if (question.length() > 10){
                DatabaseReference db = Constants.DATABASE.child("forumquestions").push();
                ForumQuestion fq = new ForumQuestion(question, Constants.USER, System.currentTimeMillis(), db.getKey());
                db.setValue(fq, (databaseError, databaseReference) -> {
                   if (databaseError == null){
                       Intent intent = new Intent(getActivity(), ForumQuestionActivity.class);
                       intent.putExtra("question", fq);
                       getActivity().startActivity(intent);
                   } else {
                       Log.i("Hilarity", databaseError.getMessage());
                   }
                });
            }
        });
        return bind.getRoot();
    }

}
