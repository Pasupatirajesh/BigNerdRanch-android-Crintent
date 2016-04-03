package secondapp.bignerdranch.com.crintent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by SSubra27 on 12/21/15.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private CrimeAdapter mAdapter;
    private int mAdapterPosition;
    private boolean mSubtitleVisible;
    private LinearLayout mEmptyViewLinearLayout;
    private int itemPosition;
    private Callbacks mCallbacks;


    private static final String CRIME_ID = "com.secondapp.bignerdranch.crimeId";
    private static final String SAVED_SUBTITLE_VISIBLE= "visible";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mRecyclerView =(RecyclerView) v.findViewById(R.id.crime_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptyViewLinearLayout=(LinearLayout)v.findViewById(R.id.emplty_title_view);
        updateUI();
        return v;
    }
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
    public interface Callbacks // Required interface for hosting activities
    {
        void onCrimeSelected(Crime crime);
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallbacks =(Callbacks)activity;
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
        updateSubtitle();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

         MenuItem subtitleItem = menu.findItem(R.id.menu_item_subtitle);
        if(mSubtitleVisible)
        {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else
        {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
//                startActivity(intent);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();

                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateSubtitle()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
//        String subtitle = getString(R.string.subtitle_format, crimeCount);
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,crimeCount,crimeCount);

        if(!mSubtitleVisible)
        {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    public void updateUI()
    {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes=crimeLab.getCrimes();
        if(mAdapter == null)
        {
            mAdapter = new CrimeAdapter(crimes);
            mRecyclerView.setAdapter(mAdapter);
        } else {
//           mAdapter.notifyItemChanged(mAdapterPosition);
                mAdapter.setCrimes(crimes);
                mAdapter.notifyItemChanged(itemPosition);


            updateSubtitle();
            if(CrimeLab.get(getActivity()).getCrimes().size()==0)
            {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyViewLinearLayout.setVisibility(View.VISIBLE);
            } else
            {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyViewLinearLayout.setVisibility(View.GONE);
            }
        }

    }
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public void bindCrime(Crime crime)
        {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_crime_checkbox);
        }
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), mCrime.getTitle() + " was Clicked", Toast.LENGTH_SHORT).show();
                itemPosition = mRecyclerView.getChildAdapterPosition(v);
//                Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
//                startActivity(intent);

                updateUI();
                mCallbacks.onCrimeSelected(mCrime);
                //UUID myId =(UUID) getActivity().getIntent().getSerializableExtra(CRIME_ID);
//                UUID currentID = mCrime.getId();
//                if(myId.equals(currentID))
//                {
//                    updateUI();
//                }
            }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes)
        {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }
        @Override
        public void onBindViewHolder(CrimeHolder holder, int position)
        {
            Crime crime = mCrimes.get(position);
//            holder.mTitleTextView.setText(crime.getTitle());
            holder.bindCrime(crime);
        }
        @Override
        public int getItemCount()
        {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes)
        {
            mCrimes=crimes;
        }
    }
}


