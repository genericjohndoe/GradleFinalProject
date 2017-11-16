package com.udacity.gradle.builditbigger.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.udacity.gradle.builditbigger.R;

/**
 * Created by joeljohnson on 11/3/17.
 */

public class NewPostDialog extends DialogFragment {

    private TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (tab.getPosition() == 1) changeFragment(new NewImagePost());
                if (tab.getPosition() == 2) changeFragment(new NewVideoPost());
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
}
