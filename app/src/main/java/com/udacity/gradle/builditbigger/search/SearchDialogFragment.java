package com.udacity.gradle.builditbigger.search;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.DialogSearchBinding;
import com.udacity.gradle.builditbigger.interfaces.EnableSearch;

public class SearchDialogFragment extends DialogFragment {

    private EnableSearch enableSearch;

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
