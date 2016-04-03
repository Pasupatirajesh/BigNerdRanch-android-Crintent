package secondapp.bignerdranch.com.crintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by SSubra27 on 12/17/15.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Date mTime;
    private String mSuspect;
    private long mCaller;

    public Crime()
    {
        this(UUID.randomUUID());
    }
    public Crime(UUID id)
    {
        mId = id;
        mDate = new Date();
        mTime = new Date();
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
    public String getSuspect()
    {
        return mSuspect;
    }
    public void setSuspect(String suspect)
    {
        mSuspect = suspect;
    }

    public long getCaller() {
        return mCaller;
    }

    public void setCaller(long caller) {
        mCaller = caller;
    }
    public String getPhotoFilename()
    {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
