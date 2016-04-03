package secondapp.bignerdranch.com.crintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by SSubra27 on 12/28/15.
 */
public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME = "passing time as frag args";
    public static final String EXTRA_TIME ="passing time back to target frag as intent extra";
    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.time_picker, null);

        Date time =(Date) getArguments().getSerializable(ARG_TIME);
        final Calendar myCal = Calendar.getInstance();
        myCal.setTime(time);

        int hour = myCal.get(Calendar.HOUR_OF_DAY);
        int min = myCal.get(Calendar.MINUTE);

        mTimePicker = (TimePicker)v.findViewById(R.id.crime_time_time_picker);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(min);



        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Time Picker").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int h = mTimePicker.getHour();
                int m = mTimePicker.getMinute();
                int y = myCal.get(Calendar.YEAR);
                int mo = myCal.get(Calendar.MONTH);
                int d = myCal.get(Calendar.DAY_OF_MONTH);

                Date date = new GregorianCalendar(y,mo,d,h,m).getTime();
                sendResult(Activity.RESULT_OK,date);
            }
        }).create();
    }
    public void sendResult(int resultCode, Date time)
    {
        if (getTargetFragment()==null)
        {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME,time);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }



}
