package com.udacity.gradle.builditbigger.messaging.mediaDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.udacity.gradle.builditbigger.interfaces.ActivityForResult;
import com.udacity.gradle.builditbigger.R;

public class ChooseMediaDialog extends DialogFragment {
    //todo pass in interface call intent creater method
    private ActivityForResult activityForResult;

    public  ChooseMediaDialog(){}

    public static ChooseMediaDialog getInstance(ActivityForResult activityForResult) {
        ChooseMediaDialog chooseMediaDialog = new ChooseMediaDialog();
        chooseMediaDialog.activityForResult = activityForResult;
        return chooseMediaDialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_choose_media, container, false);
        ImageButton photo = root.findViewById(R.id.add_photo_button);
        photo.setOnClickListener(view -> activityForResult.activityForResult(2));
        ImageButton video = root.findViewById(R.id.add_video_button);
        video.setOnClickListener(view -> activityForResult.activityForResult(3));
        ImageButton gif = root.findViewById(R.id.add_gif_button);
        gif.setOnClickListener(view -> activityForResult.activityForResult(4));
        return root;
    }

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView used to ensure max width of dialog
        dialog.setContentView(R.layout.dialog_add_media);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}
