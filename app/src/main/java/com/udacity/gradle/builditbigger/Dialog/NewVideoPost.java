package com.udacity.gradle.builditbigger.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.gradle.builditbigger.Camera.AutoFitTextureView;
import com.udacity.gradle.builditbigger.Camera.LifeCycleCamera;
import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/4/17.
 */

public class NewVideoPost extends Fragment {
    //TODO change UI to open camera and show horizontal linear recyclerview below
    //todo ensure camera is set to video
    //todo ensure camera object is deleted when fragment dies
    AutoFitTextureView textureView;
    RecyclerView recyclerView;
    LifeCycleCamera camera;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_video_post, container, false);
        textureView = root.findViewById(R.id.textureView);
        recyclerView = root.findViewById(R.id.video_thumbnail_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        camera = new LifeCycleCamera(this, textureView, LifeCycleCamera.VIDEO);
        return root;
    }
}
