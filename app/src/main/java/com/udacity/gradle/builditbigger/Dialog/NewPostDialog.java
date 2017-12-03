package com.udacity.gradle.builditbigger.Dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewPostDialog extends DialogFragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private TabLayout tabLayout;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStorageWritePermission();
            return;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_post, container, false);
        changeFragment(new NewTextPost());
        tabLayout = root.findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) changeFragment(new NewTextPost());
                if (tab.getPosition() == 1){
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("media", 1);
//                    NewMediaSubmission nms = new NewMediaSubmission();
//                    nms.setArguments(bundle);
                    changeFragment(new NewImagePost());
                }
                if (tab.getPosition() == 2){
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("media", 2);
//                    NewMediaSubmission nms = new NewMediaSubmission();
//                    nms.setArguments(bundle);
                    changeFragment(new NewVideoPost());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            //TODO allow user to submit information to the database
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });


        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView used to ensure max width of dialog
        dialog.setContentView(R.layout.dialog_new_post);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    private void changeFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction()
                .replace(R.id.new_post_fragment, fragment)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void requestStorageWritePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        }
    }
}
