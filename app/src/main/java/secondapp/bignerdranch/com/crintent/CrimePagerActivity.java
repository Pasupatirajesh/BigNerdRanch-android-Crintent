package secondapp.bignerdranch.com.crintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by SSubra27 on 12/24/15.
 */
public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks{

    private static final String EXTRA_CRIME = "secondapp.bignerdranch.crintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(Context packageContext, UUID crimeId)
    {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME, crimeId);
        return intent;
    }
    @Override
    public void onCrimeUpdated(Crime crime)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId=(UUID)getIntent().getSerializableExtra(EXTRA_CRIME);
        mViewPager = (ViewPager)findViewById(R.id.activity_crime_pager_view_pager);
        mCrimes=CrimeLab.get(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i=0; i<mCrimes.size();i++)
        {
            if(mCrimes.get(i).getId().equals(crimeId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

}
