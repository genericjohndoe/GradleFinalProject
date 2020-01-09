package com.udacity.gradle.builditbigger.signInOnboarding.userName.PickCountry;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.constants.FlagEmojiMap;
import com.udacity.gradle.builditbigger.interfaces.DismissPopUp;
import com.udacity.gradle.builditbigger.interfaces.SetFlag;
import com.udacity.gradle.builditbigger.models.Country;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * this class generates dialog box with countries' flags ready to be selected
 */
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
        //modify look of dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_countries);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    /**
     * callback fired by CountryAdapter class when item is selected
     */
    @Override
    public void dismissPopUp() {
        dismiss();
    }
}
