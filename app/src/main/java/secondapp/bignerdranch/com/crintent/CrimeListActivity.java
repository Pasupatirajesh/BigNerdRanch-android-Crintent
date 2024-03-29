package secondapp.bignerdranch.com.crintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by SSubra27 on 12/21/15.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks , CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment()
    {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime)
    {
        if(findViewById(R.id.detail_fragment_container)== null)
        {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else
        {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetail).commit();

        }
    }
    public void onCrimeUpdated(Crime crime)
    {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
    @Override
    protected int getLayoutResId()
    {
        return R.layout.activity_masterdetail;
    }

}
