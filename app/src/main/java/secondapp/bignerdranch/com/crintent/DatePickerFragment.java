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
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by SSubra27 on 12/26/15.
 */
public class DatePickerFragment extends DialogFragment {
    private static final String ARG_DATE  ="criem_date_dialog_picker";
    public static final String EXTRA_DATE = "date as an extra";
    private DatePicker mDatePicker;
    public static DatePickerFragment newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View v = layoutInflater.inflate(R.layout.date_picket, null);

        Date myDate = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar myCal = Calendar.getInstance();
        myCal.setTime(myDate);
        int year = myCal.get(Calendar.YEAR);
        int month = myCal.get(Calendar.MONTH);
        int day = myCal.get(Calendar.DAY_OF_MONTH);

        mDatePicker =(DatePicker) v.findViewById(R.id.date_picker_dialog);
        mDatePicker.init(year,month,day,null);

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year,month,day).getTime();
                sendResult(Activity.RESULT_OK,date);
            }
        })
                .create();

    }
    private void sendResult(int resultCode, Date date)
    {
        if(getTargetFragment()==null)
        {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
