package dev.m.hussein.jobtask.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import butterknife.ButterKnife;

/**
 * Created by Dev. M. Hussein on 11/5/2017.
 */

public class BaseActivity extends AppCompatActivity {



    protected static final int REQUEST_IMAGE_PICKER = 160;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private Uri profileUri;
    private OnImageReady onImageReady;


    protected void pickImage( OnImageReady onImageReady) {
        this.onImageReady = onImageReady;
        if (!checkPermissions()){
            return;
        }
        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT, null);
        galleryintent.setType("image/*");
        galleryintent.putExtra("return-data", true);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        cameraIntent.putExtra("return-data", true);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryintent);
        cameraIntent.putExtra(Intent.EXTRA_TITLE, "Select from:");

        Intent[] intentArray = { cameraIntent };
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooser, REQUEST_IMAGE_PICKER);


    }



    public boolean checkPermissions() {
        boolean needPermission =  ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ;

        if(needPermission) {
            String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

            ActivityCompat.requestPermissions( this, permissions, REQUEST_CAMERA_PERMISSION);

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {


            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage(onImageReady);
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (requestCode == REQUEST_IMAGE_PICKER) {
                if (data == null) {
                    Log.i("PATH_IMAGE", "data is null");
                }
                profileUri = data.getData();

             if (profileUri == null) return;

                CropImage.activity(profileUri)
                        .setAspectRatio(10 , 5)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                Log.i("PATH_IMAGE", "CROP_IMAGE_ACTIVITY_REQUEST_CODE");

                profileUri = result.getUri();


                if (onImageReady != null && profileUri != null && new File(profileUri.getPath()).exists()) {
                    onImageReady.setOnImageReady(profileUri);

                }


            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.i("PATH_IMAGE", "CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE");
                Log.i("PATH_IMAGE", "error : "+error.getMessage());
            }




        }



    }


    protected interface OnImageReady{
        void setOnImageReady(Uri uri);
    }

}
