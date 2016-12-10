//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Face-Android
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.microsoft.projectoxford.face.samples;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.contract.FaceRectangle;
import com.microsoft.projectoxford.face.contract.VerifyResult;
import com.microsoft.projectoxford.face.samples.helper.ImageHelper;
import com.microsoft.projectoxford.face.samples.helper.SampleApp;
import com.microsoft.projectoxford.face.samples.helper.SelectImageActivity2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DetectionActivity2 extends ActionBarActivity {
    // Background task of face detection.


    // Flag to indicate which task is to be performed.
    private static final int REQUEST_SELECT_IMAGE = 0;
    //  private ProgressDialog detectionProgressDialog;

    // The URI of the image selected to detect.
    private Uri mImageUri;

    // The image selected to detect.
    private Bitmap mBitmap;
    private Bitmap nBitmap;
    private Bitmap kBitmap;
    public static UUID id1;
    public static UUID id2;
    String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    private static int i=0;

    int permsRequestCode = 200;

    // Progress dialog popped up when communicating with server.
    ProgressDialog mProgressDialog;
    private class VerificationTask extends AsyncTask<Void, String, VerifyResult> {
        // The IDs of two face to verify.
        private UUID mFaceId0;
        private UUID mFaceId1;

        VerificationTask (UUID faceId0, UUID faceId1) {
            mFaceId0 = faceId0;
            mFaceId1 = faceId1;
        }

        @Override
        protected VerifyResult doInBackground(Void... params) {
            // Get an instance of face service client to detect faces in image.
            FaceServiceClient faceServiceClient = SampleApp.getFaceServiceClient();
            try{
                publishProgress("Verifying...");

                // Start verification.
                return faceServiceClient.verify(
                        mFaceId0,      /* The first face ID to verify */
                        mFaceId1);     /* The second face ID to verify */
            }  catch (Exception e) {
                publishProgress(e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(DetectionActivity2.this, "VERIFYING PLEASE WAIT A BIT MORE", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected void onProgressUpdate(String... progress) {
            Toast.makeText(DetectionActivity2.this, "VERIFYING PLEASE WAIT A BIT MORE", Toast.LENGTH_SHORT).show();


        }

        @Override
        protected void onPostExecute(VerifyResult result) {
            if (result != null) {

                if(result.isIdentical){Toast.makeText(DetectionActivity2.this, "Same", Toast.LENGTH_SHORT).show();
                    i=1;
                    Intent intent1 = new Intent(getApplicationContext(), SaveMyAppsService.class);
                    stopService(intent1);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                else{Toast.makeText(DetectionActivity2.this, "PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();

                }
            }

            // Show the result on screen when verification is done.

        }
    }
    private void detectAndFrame2()
    {
        final Bitmap imageBitmap=kBitmap;
        final ImageView imageView =(ImageView)findViewById(R.id.image);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());
        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            FaceServiceClient faceServiceClient =
                                    new FaceServiceRestClient("Enter Azure Key Here");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    null           // returnFaceAttributes: a string like "age, gender"
                            );
                            if (result == null)
                            {
                                publishProgress("Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(

                                    String.format("Detection Finished. %d face(s) detected",
                                            result.length));
                            return result;
                        } catch (Exception e) {

                            publishProgress("Detection failed");
                            return null;
                        }
                    }
                    @Override
                    protected void onPreExecute() {
                        Toast.makeText(DetectionActivity2.this, "LOADING MASTER IMAGE", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        Toast.makeText(DetectionActivity2.this, "VERIFYING PLEASE WAIT", Toast.LENGTH_SHORT).show();



                    }
                    @Override
                    protected void onPostExecute(Face[] result) {


                        imageView.setImageBitmap(drawFaceRectanglesOnBitmap2(imageBitmap, result));
                        new VerificationTask(id1, id2).execute();

                        imageBitmap.recycle();
                    }
                };
        detectTask.execute(inputStream);
    }
    private static Bitmap drawFaceRectanglesOnBitmap2(Bitmap originalBitmap, Face[] faces) {

        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int stokeWidth = 2;
        paint.setStrokeWidth(stokeWidth);
        if (faces != null) {

            for (Face face : faces) {
                id2=face.faceId;
                FaceRectangle faceRectangle = face.faceRectangle;

                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);

            }


        }
        return bitmap;
    }
    private void detectAndFrame()
    {   nBitmap=mBitmap.copy(Bitmap.Config.ARGB_8888, false);
        final Bitmap imageBitmap=nBitmap;
        final ImageView imageView =(ImageView)findViewById(R.id.image);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream =
                new ByteArrayInputStream(outputStream.toByteArray());
        AsyncTask<InputStream, String, Face[]> detectTask =
                new AsyncTask<InputStream, String, Face[]>() {
                    @Override
                    protected Face[] doInBackground(InputStream... params) {
                        try {
                            publishProgress("Detecting...");
                            FaceServiceClient faceServiceClient =
                                    new FaceServiceRestClient("Enter Azure Key Here");
                            Face[] result = faceServiceClient.detect(
                                    params[0],
                                    true,         // returnFaceId
                                    false,        // returnFaceLandmarks
                                    null           // returnFaceAttributes: a string like "age, gender"
                            );
                            if (result == null)
                            {
                                publishProgress("Detection Finished. Nothing detected");
                                return null;
                            }
                            publishProgress(

                                    String.format("Detection Finished. %d face(s) detected",
                                            result.length));
                            return result;
                        } catch (Exception e) {

                            publishProgress("Detection failed");
                            return null;
                        }
                    }
                    @Override
                    protected void onPreExecute() {

                    }
                    @Override
                    protected void onProgressUpdate(String... progress) {
                        Toast.makeText(DetectionActivity2.this, "Detecting", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    protected void onPostExecute(Face[] result) {
                        //TODO: update face frames

                        Face[] faces1=result;
                        if (faces1.length<=0) {

                            Toast.makeText(DetectionActivity2.this, "Not Detected Please Select another image before proceeding", Toast.LENGTH_LONG).show();
                            return;}
                        else{Toast.makeText(DetectionActivity2.this, "Image is in the system you can now proceed", Toast.LENGTH_LONG).show();}
                        imageView.setImageBitmap(drawFaceRectanglesOnBitmap(imageBitmap, result));
                        imageBitmap.recycle();
                    }
                };
        detectTask.execute(inputStream);
    }

    private static Bitmap drawFaceRectanglesOnBitmap(Bitmap originalBitmap, Face[] faces) {

        Bitmap bitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        int stokeWidth = 2;
        paint.setStrokeWidth(stokeWidth);
        if (faces != null) {

            for (Face face : faces) {
                id1=face.faceId;
                FaceRectangle faceRectangle = face.faceRectangle;

                canvas.drawRect(
                        faceRectangle.left,
                        faceRectangle.top,
                        faceRectangle.left + faceRectangle.width,
                        faceRectangle.top + faceRectangle.height,
                        paint);

            }


        }
        return bitmap;
    }
    // When the activity is created, set all the member variables to initial state.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection2);



        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(getString(R.string.progress_dialog_title));

        // Disable button "detect" as the image to detect is not selected.
        setDetectButtonEnabledStatus(false);

        Button button2 = (Button) findViewById(R.id.select_image);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);

            }
        });
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
           Toast.makeText(this, mBitmap.toString(), Toast.LENGTH_SHORT).show();
       }
       catch(Exception e)
        {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }*/


//you can create a new file name "test.jpg" in sdcard folder.
                try {
                    ImageView img = (ImageView) findViewById(R.id.image);
                    File root = Environment.getExternalStorageDirectory();
                    kBitmap = BitmapFactory.decodeFile(root+"/test.jpg");
                    img.setImageBitmap(kBitmap);
                    detectAndFrame2();


                }
                catch(Exception e)
                {
                    Toast.makeText(DetectionActivity2.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
                //Intent intent1 = new Intent(getApplicationContext(), SaveMyAppsService.class);
               // stopService(intent1);
              //  android.os.Process.killProcess(android.os.Process.myPid());

            }

        });
    }
    //@Override
    //protected void onPause() {
    //    super.onPause();
    //    finish();
    //}
    // Save the activity state when it's going to stop.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("ImageUri", mImageUri);
    }

    // Recover the saved state when the activity is recreated.
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mImageUri = savedInstanceState.getParcelable("ImageUri");
        if (mImageUri != null) {
            mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                    mImageUri, getContentResolver());
        }
    }

    // Called when image selection is done.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    // If image is selected successfully, set the image URI and bitmap.
                    mImageUri = data.getData();
                    mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                            mImageUri, getContentResolver());
                    if (mBitmap != null) {
                        // Show the image on screen.
                        ImageView imageView = (ImageView) findViewById(R.id.image);
                        imageView.setImageBitmap(mBitmap);

                    }

                    // Clear the detection result.
                    FaceListAdapter faceListAdapter = new FaceListAdapter(null);
                    //  ListView listView = (ListView) findViewById(R.id.list_detected_faces);
                    //   listView.setAdapter(faceListAdapter);

                    // Clear the information panel.
                    setInfo("");

                    // Enable button "detect" as the image is selected and not detected.
                    setDetectButtonEnabledStatus(true);
                    detectAndFrame();
                }
                break;
            default:
                break;
        }
    }

    // Called when the "Select Image" button is clicked.
    public void selectImage(View view) {
        Intent intent = new Intent(this, SelectImageActivity2.class);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);

    }

    // Called when the "Detect" button is clicked.






    // Set whether the buttons are enabled.
    private void setDetectButtonEnabledStatus(boolean isEnabled) {
        Button detectButton = (Button) findViewById(R.id.detect);
        detectButton.setEnabled(isEnabled);
    }



    // Set the information panel on screen.
    private void setInfo(String info) {
        TextView textView = (TextView) findViewById(R.id.info);
        textView.setText(info);
    }




    private class FaceListAdapter extends BaseAdapter {
        // The detected faces.
        List<Face> faces;

        // The thumbnails of detected faces.
        List<Bitmap> faceThumbnails;

        // Initialize with detection result.
        FaceListAdapter(Face[] detectionResult) {
            faces = new ArrayList<>();
            faceThumbnails = new ArrayList<>();

            if (detectionResult != null) {
                faces = Arrays.asList(detectionResult);
                for (Face face : faces) {
                    try {
                        // Crop face thumbnail with five main landmarks drawn from original image.
                        faceThumbnails.add(ImageHelper.generateFaceThumbnail(
                                mBitmap, face.faceRectangle));
                    } catch (IOException e) {
                        // Show the exception when generating face thumbnail fails.
                        setInfo(e.getMessage());
                    }
                }
            }
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public int getCount() {
            return faces.size();
        }

        @Override
        public Object getItem(int position) {
            return faces.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item_face_with_description, parent, false);
            }
            convertView.setId(position);

            // Show the face thumbnail.
            ((ImageView) convertView.findViewById(R.id.face_thumbnail)).setImageBitmap(
                    faceThumbnails.get(position));

            // Show the face details.
            DecimalFormat formatter = new DecimalFormat("#0.0");
            String face_description = "Age: " + formatter.format(faces.get(position).faceAttributes.age) + "\n"
                    + "Gender: " + faces.get(position).faceAttributes.gender + "\n"
                    + "Head pose(in degree): roll(" + formatter.format(faces.get(position).faceAttributes.headPose.roll) + "), "
                    + "yaw(" + formatter.format(faces.get(position).faceAttributes.headPose.yaw) + ")\n"
                    + "Glasses: " + faces.get(position).faceAttributes.glasses + "\n"
                    + "Smile: " + formatter.format(faces.get(position).faceAttributes.smile);
            ((TextView) convertView.findViewById(R.id.text_detected_face)).setText(face_description);

            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(i==0)
        {  Intent intent = new Intent(getApplicationContext(),SaveMyAppsService.class);
            startService(intent);
        }
    }
}
