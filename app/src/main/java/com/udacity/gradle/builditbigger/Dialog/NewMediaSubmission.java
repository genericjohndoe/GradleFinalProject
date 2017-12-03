package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/21/17.
 */

public class NewMediaSubmission extends Fragment {
    //todo why does this class exist?
    private int typeOfFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeOfFragment = getArguments().getInt("media");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_framelayout, container, false);
        getChildFragmentManager().beginTransaction()
                .add(R.id.dialog_frame_layout, typeOfFragment == 1 ? new NewImagePost(): new NewVideoPost() )
                .addToBackStack(null).commit();
        return root;
    }
}
