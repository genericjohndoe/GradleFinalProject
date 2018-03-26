package com.udacity.gradle.builditbigger.Messaging.SentMessages;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Constants.Constants;
import com.udacity.gradle.builditbigger.Messaging.ComposeMessage.ComposeMessageFragment;
import com.udacity.gradle.builditbigger.Models.TranscriptPreview;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.FragmentSentMessageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SentMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 * shows list of transcript previews indicating conversation among different users in reverse chronological order
 */
public class SentMessagesFragment extends Fragment {
    private String uid;

    public SentMessagesFragment() {
        // Required empty public constructor
    }

    public static SentMessagesFragment newInstance(String uid) {
        SentMessagesFragment fragment = new SentMessagesFragment();
        Bundle args = new Bundle();
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) uid = getArguments().getString("uid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Sent Messages");
        List<TranscriptPreview> transcriptPreviews = new ArrayList<>();
        SentMessagesAdapter sentMessagesAdapter = new SentMessagesAdapter(transcriptPreviews, getActivity());
        FragmentSentMessageBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sent_message, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        //llm.setStackFromEnd(true);
        binding.sentMessagesRecyclerview.setLayoutManager(llm);
        binding.sentMessagesRecyclerview.setAdapter(sentMessagesAdapter);

        binding.newMessageFab.setOnClickListener(view -> {
            Constants.changeFragment(R.id.hilarity_content_frame, ComposeMessageFragment.newInstance(Constants.UID), (AppCompatActivity) getActivity());
        });

        SentMessagesViewModel sentMessagesViewModel = ViewModelProviders.of(this, new SentMessagesViewModelFactory(uid)).get(SentMessagesViewModel.class);
        sentMessagesViewModel.getSentMessagesLiveData().observe(this, transcriptPreview -> {
            transcriptPreviews.add(transcriptPreview);
            sentMessagesAdapter.notifyDataSetChanged();
        });
        return binding.getRoot();
    }

}