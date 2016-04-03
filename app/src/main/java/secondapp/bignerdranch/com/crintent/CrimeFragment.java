package secondapp.bignerdranch.com.crintent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by SSubra27 on 12/17/15.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mEditText;
    private TextView mTextView;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private DateFormat mDateFormat;
    private DateFormat mTimeFormat;
    private Button mReportButton;
    private Button mSuspectButton;
    private Button mCallSuspectButton;
    private Cursor contactCursor;
    private ImageView mPhotoView;
    private Point mPhotoViewSize;
    private ImageButton mPhotoButton;
    private File mPhotoFile;
    private Callbacks mCallbacks;

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DATE_PICKER_DIALOG = "Date_picker";
    private static final String PHOTO_PICKER_DIALOG = "photo_picker";
    public static final String TIME_PICKER_ACTIVITY="time_picker";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;



    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {  // mCrime = new Crime(); taken out so that we are able to see the changes made in CrimeFragment show up on CrimeListFragment
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile=CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        setHasOptionsMenu(true);
    }
    public interface Callbacks
    {
        void onCrimeUpdated(Crime crime);
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }
    @Override
    public void onPause()
    {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mCallbacks = null;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.fragment_crime_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_crime_delete:
                Toast.makeText(getActivity(), mCrime.getTitle() + " is being deleted", Toast.LENGTH_SHORT).show();
                if(mCrime!=null)
                CrimeLab.get(getActivity()).deleteCrime(mCrime.getId()); // Intent taken out because I am using the finish method to take me back to the top of the Activity Backstack
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, final Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.fragment_crime, container, false);
        mEditText = (EditText) v.findViewById(R.id.crime_title);
        mEditText.setText(mCrime.getTitle());
        updateCrime();
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DATE_PICKER_DIALOG);
            }
        });
        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        mTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.US);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DatePickerActivity.newIntent(getActivity(), mCrime.getTime());
                startActivityForResult(intent, REQUEST_TIME);
//                FragmentManager fm = getFragmentManager();
//                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getTime());
//                timePickerFragment.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
//                timePickerFragment.show(fm,TIME_PICKER_DIALOG);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });
        mReportButton = (Button) v.findViewById(R.id.send_crime_suspect_button);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(getActivity()) // This lets us not build the Intent manually if we use inner class ShareCompat.IntentBuilder
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setChooserTitle(R.string.send_report)
                        .startChooser();
            }
        });
        mSuspectButton = (Button) v.findViewById(R.id.choose_crime_suspect_button);
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(pickContact, REQUEST_CONTACT);
            }

        });
        mCallSuspectButton = (Button) v.findViewById(R.id.call_contact);
        int checkSelfPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS);
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            CrimeFragment.this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT);

        } else
        {
            PackageManager packageMan = getActivity().getPackageManager();
        if (packageMan.hasSystemFeature(packageMan.FEATURE_TELEPHONY)) {
            mCallSuspectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactCursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{Long.toString(mCrime.getCaller())}, null);
                    if (contactCursor != null && contactCursor.getCount() > 0) {
                        Toast.makeText(getActivity(), "Retrieving Contact Number", Toast.LENGTH_SHORT).show();
                        try {
                            contactCursor.moveToFirst();
                            String number = contactCursor.getString(0);
                            Uri phoneNumber = Uri.parse("tel:5556765" + number);
                            Intent intent = new Intent(Intent.ACTION_DIAL, phoneNumber);
                            startActivity(intent);
                        } finally {
                            contactCursor.close();
                        }
                    }
                }
            });
        }
    }
        if(mCrime.getSuspect()!=null)
        {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)== null)
        {
            mSuspectButton.setEnabled(false);
        }
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile !=null && captureImage.resolveActivity(packageManager)!=null;
        if(canTakePhoto)
        {
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView)v.findViewById(R.id.crime_photo);
     mPhotoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()

     {
         @Override
         public void onGlobalLayout() {
             boolean isFirstPass = (mPhotoViewSize == null);
             mPhotoViewSize = new Point();
             mPhotoViewSize.set(mPhotoView.getMaxWidth(), mPhotoView.getMaxHeight());
             if (isFirstPass) {
                 updatePhotoView();
             }
         }
     });

//updatePhotoView();
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPhotoFile!= null && mPhotoFile.exists())
                {
                    FragmentManager fragmentManager = getFragmentManager();
                    PhotoDialogPickerFragment photoDialogPickerFragment = PhotoDialogPickerFragment.newInstance(mPhotoFile);
                    photoDialogPickerFragment.show(fragmentManager,PHOTO_PICKER_DIALOG);
                }
            }
        });

        return v;
    }

    private void updateCrime()
    {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }
    private void updateDate() {
        mDateButton.setText(mDateFormat.format(mCrime.getDate()));
    }
    private void updateTime() {
        mTimeButton.setText(mTimeFormat.format(mCrime.getTime()));
    }
    private String getCrimeReport()
    {
        String solvedString = null;
        if(mCrime.isSolved())
        {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString=getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if(suspect== null)
        {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,mCrime.getTitle(),dateString,solvedString,suspect);
        return report;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            updateDate();

        } else if (requestCode == REQUEST_TIME) {
            Date time = (Date) data.getSerializableExtra(DatePickerFragmentActivity.EXTRA_TIME_SENT);
            mCrime.setTime(time);
            updateTime();
            updateCrime();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            //specify which fields you want your query to return values for//
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID};
            // Perform your query - the contactUri is like a "where" // clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
            try {
                // Double check that u actually got the results//
                if (c.getCount() == 0) {
                    return;
                }     // pull out the first column of the first row of data, that is your suspect's name//
                    c.moveToFirst();
                    String suspect = c.getString(0);
                    long contactId = c.getLong(1);
                    mCrime.setCaller(contactId);
                    mCrime.setSuspect(suspect);
                updateCrime();
                    mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        } else if(requestCode == REQUEST_PHOTO)
        {
            updateCrime();
            updatePhotoView();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode==REQUEST_CONTACT)
        {
            if(grantResults.length> 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Intent pickContact = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        }
    }
    private void updatePhotoView()
    {
        if(mPhotoFile == null || !mPhotoFile.exists())
        {
            mPhotoView.setImageDrawable(null);

        } else
        {
            Bitmap bitmap =(mPhotoViewSize==null) ? PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity()): PictureUtils.getScaledBitmap(mPhotoFile.getPath(),mPhotoViewSize.x,mPhotoViewSize.y);
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setClickable(true);
        }
    }
}
