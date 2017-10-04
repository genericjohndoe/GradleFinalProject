package com.udacity.gradle.builditbigger.UserSpecific;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.udacity.gradle.builditbigger.R;

import agency.tango.materialintroscreen.SlideFragment;

/**
 * Created by joeljohnson on 10/3/17.
 */

public class ChooseUserNameFragment extends SlideFragment {
    EditText editText;
    ImageButton imageButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_choose_username, container, false);
        editText = view.findViewById(R.id.username_edittext);
        imageButton = view.findViewById(R.id.validate);

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.primary;
    }

    @Override
    public int buttonsColor() {
        return R.color.accent;
    }

    @Override
    public boolean canMoveFurther() {
        return true;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "No Language Selected";
    }
}
