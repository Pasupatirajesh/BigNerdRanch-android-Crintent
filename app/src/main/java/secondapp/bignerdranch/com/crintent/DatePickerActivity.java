package secondapp.bignerdranch.com.crintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by SSubra27 on 12/29/15.
 */
public class DatePickerActivity extends SingleFragmentActivity {

    public static final String EXTRA_TIME = "time to be got from crimefragment";

    @Override
    protected Fragment createFragment(){
        Date time=(Date)getIntent().getSerializableExtra(EXTRA_TIME);

        return DatePickerFragmentActivity.newInstance(time);
   }

    public static Intent newIntent(Context packageContext,Date time)
    {
        Intent intent = new Intent(packageContext,DatePickerActivity.class);
        intent.putExtra(EXTRA_TIME,time);
        return intent;
    }
}
