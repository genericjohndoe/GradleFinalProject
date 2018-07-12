package com.udacity.gradle.builditbigger.SignInTutorial.UserName.PickCountry;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.udacity.gradle.builditbigger.Constants.FlagEmojiMap;
import com.udacity.gradle.builditbigger.Interfaces.DismissPopUp;
import com.udacity.gradle.builditbigger.Interfaces.SetFlag;
import com.udacity.gradle.builditbigger.Models.Country;
import com.udacity.gradle.builditbigger.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CountriesPopUpDialogFragment extends DialogFragment implements DismissPopUp {
    private SetFlag setFlag;
    private List<Country> countryList = new ArrayList<>();
    private FlagEmojiMap flagEmojiMap = FlagEmojiMap.getInstance();


    public static CountriesPopUpDialogFragment getInstance(SetFlag setFlag){
        CountriesPopUpDialogFragment pop = new CountriesPopUpDialogFragment();
        pop.setFlag = setFlag;
        return pop;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (Map.Entry<String,String> entry : flagEmojiMap.entrySet()){
            countryList.add(new Country(entry.getKey(),entry.getValue()));
        }
        Log.i("countries", ""+countryList.size());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_countries, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new CountryAdapter(setFlag, this, countryList));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView used to ensure max width of dialog
        dialog.setContentView(R.layout.dialog_countries);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }


    @Override
    public void dismissPopUp() {
        dismiss();
    }
}
