package secondapp.bignerdranch.com.crintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by SSubra27 on 12/29/15.
 */
public class DatePickerFragmentActivity extends DialogFragment {
    private static final String EXTRA_TIME="time received";
    public static final String EXTRA_TIME_SENT = "time sent back to requesting activity";

    private Button mOkButton;
    private TimePicker mTimePicker;
    private TextView mTitleView;


    public static DatePickerFragmentActivity newInstance(Date time)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, time);
        DatePickerFragmentActivity datePickerFragmentActivity = new DatePickerFragmentActivity();
        datePickerFragmentActivity.setArguments(args);
        return datePickerFragmentActivity;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater,ViewGroup container,Bundle savedInstanceState)
    {

        Date time = (Date) getArguments().getSerializable(EXTRA_TIME);
        final Calendar myCal = Calendar.getInstance();
        myCal.setTime(time);

        int ho = myCal.get(Calendar.HOUR_OF_DAY);
        int min = myCal.get(Calendar.MINUTE);
        View v = layoutInflater.inflate(R.layout.dialog_date, container, false);


        mTimePicker=(TimePicker)v.findViewById(R.id.dialog_time_time_picker_picker);
        mTimePicker.setCurrentHour(ho);
        mTimePicker.setCurrentMinute(min);

        mTitleView=(TextView)v.findViewById(R.id.time_picker_title_view);
        mTitleView.setText("Time Picker");
        mOkButton=(Button) v.findViewById(R.id.ok_button);
        mOkButton.setText("OK");
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int y = myCal.get(Calendar.YEAR);
                int m = myCal.get(Calendar.MONTH);
                int d = myCal.get(Calendar.DAY_OF_MONTH);
                int hour = mTimePicker.getHour();
                int mi = mTimePicker.getMinute();

                Date date = new GregorianCalendar(y,m,d,hour,mi).getTime();
                SendResult(Activity.RESULT_OK,date);

            }
        });
        return v;

    }
    public void SendResult(int resultCode, Date date)
    {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME_SENT, date);
        if(getTargetFragment()==null)
        {
            getActivity().setResult(resultCode, intent);
            getActivity().finish();
        }


    }
}
