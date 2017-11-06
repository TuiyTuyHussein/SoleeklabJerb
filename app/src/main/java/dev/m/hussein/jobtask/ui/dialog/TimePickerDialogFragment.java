package dev.m.hussein.jobtask.ui.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Dev. M. Hussein on 10/26/2017.
 */

public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private  OnDateSet onDateSet;
    private Calendar c;
    private TimePickerDialog timePicker;

    public void setOnDateSet(Calendar calendar  , OnDateSet onDateSet) {
        this.onDateSet = onDateSet;
        this.c = calendar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (c == null) c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        timePicker =  new TimePickerDialog(getActivity() , this , hourOfDay , minute , false);
        // Create a new instance of DatePickerDialog and return it
        return timePicker;


    }



    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY , hourOfDay);
        c.set(Calendar.MINUTE , minute);

        if (onDateSet != null) onDateSet.setOnDateSet(this , c);
    }



    public static interface OnDateSet{
        void setOnDateSet(DialogFragment dialog, Calendar calendar);
    }
}
