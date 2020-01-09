package com.udacity.gradle.builditbigger.forums.createQuestion;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.Constants;
import com.udacity.gradle.builditbigger.databinding.FragmentNewQuestionBinding;
import com.udacity.gradle.builditbigger.forums.replies.ForumQuestionActivity;
import com.udacity.gradle.builditbigger.models.ForumQuestion;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewQuestionFragment extends Fragment {
    private String contents;
    private String key;

    public NewQuestionFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewQuestionFragment.
     */
    public static NewQuestionFragment newInstance(String contents, String key) {
        NewQuestionFragment newQuestionFragment = new NewQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("contents", contents);
        bundle.putString("key", key);
        newQuestionFragment.setArguments(bundle);
        return newQuestionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            contents = getArguments().getString("contents");
            key = getArguments().getString("key");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNewQuestionBinding bind = DataBindingUtil.inflate(inflater,R.layout.fragment_new_question, container, false);
        if (contents != null) bind.questionEditText.setText(contents);
        bind.questionEditText.requestFocus();
        bind.submitButton.setOnClickListener(view ->{
            String question = bind.questionEditText.getText().toString().trim();
            if (question.length() > 10 && (key == null)){
                DatabaseReference db = Constants.DATABASE.child("forumquestions").push();
                ForumQuestion fq = new ForumQuestion(question, Constants.UID, System.currentTimeMillis(), db.getKey());
                db.setValue(fq, (databaseError, databaseReference) -> {
                   if (databaseError == null){
                       Intent intent = new Intent(getActivity(), ForumQuestionActivity.class);
                       intent.putExtra("key", fq.getKey());
                       startActivity(intent);
                       getActivity().finish();
                   }
                });
            } else if (question.length() > 10 && (key != null)) {
                Constants.DATABASE.child("forumquestions/"+key+"/question").setValue(question, (databaseError, databaseReference) -> {
                    if (databaseError == null){
                        Intent intent = new Intent(getActivity(), ForumQuestionActivity.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });
        return bind.getRoot();
    }

}
