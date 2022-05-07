package com.example.applicationmarsh.Fragments.Claim;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.applicationmarsh.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class CarRegisterStep2 extends Fragment {

    static Map<String, CheckBox> checkBoxes = new HashMap<>();
    static Map<String, String> damagePhotos = new HashMap<>();

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST_1 = 1881;
    private static final int CAMERA_REQUEST_2 = 1882;
    private static final int CAMERA_REQUEST_3 = 1883;
    private static final int CAMERA_REQUEST_4 = 1884;

    private static String photoEncoded1 = "";
    private static String photoEncoded2 = "";
    private static String photoEncoded3 = "";
    private static String photoEncoded4 = "";

    private ImageView damagePhoto1;
    private ImageView damagePhoto2;
    private ImageView damagePhoto3;
    private ImageView damagePhoto4;

    private Uri imageUri1;
    private Uri imageUri2;
    private Uri imageUri3;
    private Uri imageUri4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_register_step_2, container, false);

        //Init items
        initItems(view);

        //Init listeners
        initListeners(view);

        return view;
    }

    //Get result from camera -> set to imageview
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            //Get photo from uri
            Bitmap photo = null;
            try {
                switch (requestCode) {
                    case CAMERA_REQUEST_1:
                        photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri1);
                        break;
                    case CAMERA_REQUEST_2:
                        photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri2);
                        break;
                    case CAMERA_REQUEST_3:
                        photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri3);
                        break;
                    case CAMERA_REQUEST_4:
                        photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri4);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Compress image to 50% quality
            if (photo == null) return;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byteArray = stream.toByteArray();

            //Scale photo down - lower lags
            int resolution = (int) (photo.getHeight() * (512.0 / photo.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(photo, 512, resolution, true);

            //Set image
            switch (requestCode) {
                case CAMERA_REQUEST_1: {
                    damagePhoto1.setImageBitmap(scaled);
                    photoEncoded1 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    break;
                }
                case CAMERA_REQUEST_2: {
                    damagePhoto2.setImageBitmap(scaled);
                    photoEncoded2 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    break;
                }
                case CAMERA_REQUEST_3: {
                    damagePhoto3.setImageBitmap(scaled);
                    photoEncoded3 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    break;
                }
                case CAMERA_REQUEST_4: {
                    damagePhoto4.setImageBitmap(scaled);
                    photoEncoded4 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    break;
                }
            }
        }
    }

    //Init image view listeners
    private void initListeners(View view) {
        damagePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    //Init uri
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "img_2");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Claim picture 2");
                    imageUri1 = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    //Init intent
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_1);
                }
            }
        });

        damagePhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    //Init uri
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "img_1");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Claim picture 1");
                    imageUri2 = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    //Init intent
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_2);
                }
            }
        });

        damagePhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    //Init uri
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "img_3");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Claim picture 3");
                    imageUri3 = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    //Init intent
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri3);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_3);
                }
            }
        });

        damagePhoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Init uri
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "img_4");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Claim picture 4");
                imageUri4 = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                //Init intent
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri4);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_4);
            }
        });

    }

    //Check permission -> Camera & External Storage
    private boolean checkPermissions() {
        if ((ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_CAMERA_PERMISSION_CODE);
            return false;
        } else {
            return true;
        }
    }

    //Init layout items
    private void initItems(View view) {
        //Init checkboxs
        CheckBox damageCar = view.findViewById(R.id.damage_car);
        CheckBox damageCarOther = view.findViewById(R.id.damage_car_other);
        CheckBox damageOther = view.findViewById(R.id.damage_other);

        //Init imageviews
        damagePhoto1 = view.findViewById(R.id.damage_photo_1);
        damagePhoto2 = view.findViewById(R.id.damage_photo_2);
        damagePhoto3 = view.findViewById(R.id.damage_photo_3);
        damagePhoto4 = view.findViewById(R.id.damage_photo_4);

        //Add to global list
        checkBoxes.put("damageCar", damageCar);
        checkBoxes.put("damageCarOther", damageCarOther);
        checkBoxes.put("damageOther", damageOther);
    }

    //Init damage photos
    public static void generatePhotosMap() {
        if (!photoEncoded1.isEmpty())
            damagePhotos.put("photo1", photoEncoded1);
        if (!photoEncoded2.isEmpty())
            damagePhotos.put("photo2", photoEncoded2);
        if (!photoEncoded3.isEmpty())
            damagePhotos.put("photo3", photoEncoded3);
        if (!photoEncoded4.isEmpty())
            damagePhotos.put("photo4", photoEncoded4);
    }
}
