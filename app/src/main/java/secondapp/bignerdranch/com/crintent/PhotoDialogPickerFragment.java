package secondapp.bignerdranch.com.crintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by SSubra27 on 1/27/16.
 */
public class PhotoDialogPickerFragment extends DialogFragment {
    private static final String PHOTO_ARG = "photo";

    public static PhotoDialogPickerFragment newInstance(File mPhotoFile)
    {
        Bundle args = new Bundle();
        args.putSerializable(PHOTO_ARG,mPhotoFile);
        PhotoDialogPickerFragment fm = new PhotoDialogPickerFragment();
        fm.setArguments(args);
        return fm;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        File imageFile =(File) getArguments().getSerializable(PHOTO_ARG);
        Bitmap imageBitmap = PictureUtils.getScaledBitmap(imageFile.getPath(),getActivity());
        View v = layoutInflater.inflate(R.layout.diaolog_picture, null);
        ImageView imageView = (ImageView)v.findViewById(R.id.suspect_image);
        imageView.setImageBitmap(imageBitmap);

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Image View").setPositiveButton(android.R.string.ok,null).create();



    }
}
