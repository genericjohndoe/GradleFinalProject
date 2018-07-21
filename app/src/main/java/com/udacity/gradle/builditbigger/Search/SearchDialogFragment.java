package com.udacity.gradle.builditbigger.Search;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.udacity.gradle.builditbigger.Interfaces.EnableSearch;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.DialogSearchBinding;

public class SearchDialogFragment extends DialogFragment {

    EnableSearch enableSearch;

    public static SearchDialogFragment getInstance(EnableSearch enableSearch){
        SearchDialogFragment searchDialogFragment = new SearchDialogFragment();
        searchDialogFragment.enableSearch = enableSearch;
        return searchDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogSearchBinding bind = DataBindingUtil.inflate(inflater, R.layout.dialog_search, container, false);
        bind.searchButton.setOnClickListener(view -> {
            enableSearch.search(bind.searchEditText.getText().toString());
            dismiss();
        });
        bind.cancelButton.setOnClickListener(view -> dismiss());
        return bind.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView used to ensure max width of dialog
        dialog.setContentView(R.layout.dialog_search);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }
}
