package secondapp.bignerdranch.com.crintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by SSubra27 on 1/26/16.
 */
public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight)
    {
        // Read in dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Figure out how much to scale down by
        int inSampleSize = 1;
        if(srcHeight>destHeight || srcWidth>destWidth)
        {
            if(srcWidth>srcHeight)
            {
                inSampleSize = Math.round(srcHeight/destHeight);

            } else
            {
                inSampleSize = Math.round(srcWidth/destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Read in and create final bitmap
        return BitmapFactory.decodeFile(path,options);

    }
    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }
}
