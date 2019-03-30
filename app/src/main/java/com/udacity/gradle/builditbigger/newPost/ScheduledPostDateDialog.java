package com.udacity.gradle.builditbigger.newPost;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.autofill.AutofillValue;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.databinding.DialogScheduledPostBinding;
import com.udacity.gradle.builditbigger.interfaces.SetDate;

import java.util.Calendar;

public class ScheduledPostDateDialog extends DialogFragment implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    private SetDate setDate;
    private DialogScheduledPostBinding binding;
    private int year, month, day, hour, minute;

    public static ScheduledPostDateDialog getInstance(SetDate setDate){
        ScheduledPostDateDialog scheduledPostDialog = new ScheduledPostDateDialog();
        scheduledPostDialog.setDate = setDate;
        Log.i("timeset", "dialog opened");
        return scheduledPostDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_scheduled_post, container, false);
        binding.sumbitButton.setOnClickListener(view -> {if (validate()){
            setDate.confirm();
            this.dismiss();
            Log.i("timeset", "submit pressed");
        }});

        binding.datePicker.setMinDate(System.currentTimeMillis());
        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        binding.datePicker.init(year, month, day, this);
        binding.timePicker.setOnTimeChangedListener(this);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView used to ensure max width of dialog
        dialog.setContentView(R.layout.dialog_scheduled_post);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        setDate.setDate(year, month, day, hour, minute);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
        hour = hourOfDay;
        this.minute = minute;
        Log.i("timeset", "hours " + hourOfDay + " minute " + minute);
        setDate.setDate(year, month, day, hourOfDay, minute);
    }

    public boolean validate(){
        Calendar futureDate = Calendar.getInstance();
        futureDate.set(year, month, day, hour, minute);
        Calendar cal = Calendar.getInstance();
        Log.i("timeset", "year " + year + " month " + month + " day " + day + " hour " + hour + " minute " + minute);
        Log.i("timeset", "year " + cal.get(Calendar.YEAR) + " month " + cal.get(Calendar.MONTH) + " day " + cal.get(Calendar.DAY_OF_MONTH)
            + " hour " + cal.get(Calendar.HOUR_OF_DAY) + " minute " + cal.get(Calendar.MINUTE));
        Log.i("timeset", "" + (futureDate.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()));
        Log.i("timeset", ""+futureDate.getTimeInMillis());
        Log.i("timeset", ""+System.currentTimeMillis());

        return futureDate.getTimeInMillis() > System.currentTimeMillis();
    }
}
