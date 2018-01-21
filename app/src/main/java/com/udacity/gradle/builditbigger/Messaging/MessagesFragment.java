package com.udacity.gradle.builditbigger.Messaging;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.udacity.gradle.builditbigger.Messaging.ComposeMessageFragment;
import com.udacity.gradle.builditbigger.R;


/**
 *
 */
public class MessagesFragment extends Fragment {

    ImageButton composeButton;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerView = root.findViewById(R.id.messages_recyclerview);
        composeButton = root.findViewById(R.id.new_message_imagebutton);
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.hilarity_content_frame, new ComposeMessageFragment(), "compose message")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }








}
