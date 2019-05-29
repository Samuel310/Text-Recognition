package com.samapps310.textrecognition.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.samapps310.textrecognition.MainActivity;
import com.samapps310.textrecognition.Permission;
import com.samapps310.textrecognition.R;

import java.io.IOException;

import static android.support.constraint.Constraints.TAG;

public class MainLayout extends Fragment {

    private ImageView imageView;
    private TextView label;
    private Button btnTakeSnap, btnSelectImage;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int READ_REQUEST_CODE = 42;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    private Permission permission;

    public MainLayout() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_layout, container, false);

        permission = new Permission(getActivity());

        imageView = view.findViewById(R.id.iv_image_holder);
        label = view.findViewById(R.id.tv_label);

        btnTakeSnap = view.findViewById(R.id.btn_take_snap);
        btnTakeSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeSnapShot();
            }
        });

        btnSelectImage = view.findViewById(R.id.btn_select_image);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permission.grantAccess(R.string.dialog_msg_to_load_image);
                if (permission.isAccessGranted()){
                    selectImageFromStorage();
                }
            }
        });



        return view;
    }

    /**
     * Method to take capture image from other camera app.
     */
    private void takeSnapShot() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Method to choose any one image from internal or external storage.
     */
    private void selectImageFromStorage(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * This method will be called if the image is captured using camera.
     * @param imageBitmap
     */
    private void firebaseVisionImageFromBitmap(Bitmap imageBitmap){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        recognizeText(image);
    }

    /**
     * This method will be called if the image is selected from internal or external storage.
     * @param uri
     */
    private void firebaseVisionImageFromFile(Uri uri){
        FirebaseVisionImage image;
        try {
            image = FirebaseVisionImage.fromFilePath(getActivity(), uri);
            recognizeText(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to extract the text from the image
     * @param image
     */
    private void recognizeText(FirebaseVisionImage image){
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        detector.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        processText(firebaseVisionText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "No text found", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * This method is used to process the text and send the result (text) to ResultLayout fragment
     * @param firebaseVisionText
     */
    private void processText(FirebaseVisionText firebaseVisionText) {
        if(firebaseVisionText.getTextBlocks().isEmpty()){
            Toast.makeText(getActivity(), "No text found or Text may not be clear", Toast.LENGTH_LONG).show();
        }
        else {
            String text = "";
            for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()){
                text = text + block.getText() + " ";
            }

            String tag = ((MainActivity)getActivity()).getTag_ResultLayout();
            ResultLayout fragment = (ResultLayout)getActivity().getSupportFragmentManager()
                    .findFragmentByTag(tag);
            fragment.setResult(text);
            ((MainActivity)getActivity()).openResultLayout();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            label.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(imageBitmap);
            firebaseVisionImageFromBitmap(imageBitmap);
        }
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                label.setVisibility(View.INVISIBLE);
                imageView.setImageURI(uri);
                firebaseVisionImageFromFile(uri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permission.setAccessGranted(true);
                } else {
                    permission.setAccessGranted(false);
                }
                return;
            }
        }
    }
}
