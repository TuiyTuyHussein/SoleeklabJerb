package dev.m.hussein.jobtask.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Dev. M. Hussein on 10/26/2017.
 */

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private  OnDateSet onDateSet;
    private Calendar c;

    public void setOnDateSet(Calendar calendar  , OnDateSet onDateSet) {
        this.onDateSet = onDateSet;
        this.c = calendar;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (c == null) c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR , year);
        calendar.set(Calendar.MONTH , month);
        calendar.set(Calendar.DAY_OF_MONTH , day);

        if (onDateSet != null) onDateSet.setOnDateSet(this , calendar);
    }

    public static interface OnDateSet{
        void setOnDateSet(DialogFragment dialog, Calendar calendar);
    }
}
