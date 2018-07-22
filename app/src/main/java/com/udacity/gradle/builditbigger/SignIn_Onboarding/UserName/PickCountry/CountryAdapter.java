package com.udacity.gradle.builditbigger.SignIn_Onboarding.UserName.PickCountry;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.gradle.builditbigger.Interfaces.DismissPopUp;
import com.udacity.gradle.builditbigger.Interfaces.SetFlag;
import com.udacity.gradle.builditbigger.Models.Country;
import com.udacity.gradle.builditbigger.R;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
    private SetFlag setFlag;
    private DismissPopUp dismissPopUp;
    private List<Country> countries;

    public CountryAdapter(SetFlag flag, DismissPopUp dismissPopUp, List<Country> countries){
        setFlag = flag;
        this.dismissPopUp = dismissPopUp;
        this.countries = countries;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_textview, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.isoCode = country.getTwoDigit();
        holder.emojiText = country.getEmoji();
        holder.emojiView.setText(holder.emojiText);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String isoCode;
        private String emojiText;
        private TextView emojiView;

        public CountryViewHolder(View view){
            super(view);
            emojiView = view.findViewById(R.id.country_textView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            setFlag.setFlag(emojiText);
            dismissPopUp.dismissPopUp();
        }
    }
}
