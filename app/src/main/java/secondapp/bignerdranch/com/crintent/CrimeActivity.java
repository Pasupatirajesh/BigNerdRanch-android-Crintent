package secondapp.bignerdranch.com.crintent;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity{
    public static final String EXTRA_CRIME_ID = "com.secondapp.bignerdranch.crintent";

    @Override
    protected Fragment createFragment()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        UUID crimeId =(UUID) getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }
    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_masterdetail;
    }
    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        Intent intent  = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }









    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crime, menu);
        return true;
    }


}
