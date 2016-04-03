package secondapp.bignerdranch.com.crintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import secondapp.bignerdranch.com.crintent.database.CrimeBaseHelper;
import secondapp.bignerdranch.com.crintent.database.CrimeCursorWrapper;
import secondapp.bignerdranch.com.crintent.database.CrimeDbSchema.CrimeTable;

/**
 * Created by SSubra27 on 12/21/15.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;
//
//    public static CrimeLab get(Context context) // one get method, it creates an instance of the class and returns it if one doesnt exist already
//    {
//        if(sCrimeLab==null)
//        {
//            sCrimeLab = new CrimeLab(context);
//
//        }
//        return sCrimeLab;
//    }
//    private CrimeLab(Context context) // private Constructor
//    {
//        mCrimes = new ArrayList<>();
//    }
//    public List<Crime> getCrimes()
//    {
//        return mCrimes;
//    }
//    public Crime getCrime(UUID id)
//    {
//        for(Crime crime: mCrimes)
//        {
//            if(crime.getId().equals(id))
//            {
//                return crime;
//            }
//        }
//        return null;
//    }

    public static CrimeLab get(Context context)
    {
        if(sCrimeLab==null)
        {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }
    public List<Crime> getCrimes()
    {
//        return new ArrayList<>();
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }
    public Crime getCrime(UUID id)
    {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        try {
            if(cursor.getCount() == 0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public  void addCrime(Crime c)
    {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }
    public void deleteCrime(UUID crimeId)
    {
        String whereArgs = crimeId.toString();
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ? ", new String[]{whereArgs});

    }
    private static ContentValues getContentValues(Crime crime)
    {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved() ? 1:0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return values;
    }
//    private Cursor queryCrimes(String whereClause, String[] whereArgs)
    private CrimeCursorWrapper queryCrimes(String whereClause,String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }
    public void updateCrime(Crime crime)
    {
        String uuidString  = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + "= ?", new String[] {uuidString});
    }
    public File getPhotoFile(Crime crime)
    {
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir == null)
        {
            return null;
        }
        return new File(externalFilesDir, crime.getPhotoFilename());
    }
}
