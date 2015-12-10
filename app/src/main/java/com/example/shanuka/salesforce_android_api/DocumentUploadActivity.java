package com.example.shanuka.salesforce_android_api;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shanuka.salesforce_android_api.camera.AlbumStorageDirFactory;
import com.example.shanuka.salesforce_android_api.camera.BaseAlbumDirFactory;
import com.example.shanuka.salesforce_android_api.camera.FroyoAlbumDirFactory;
import com.example.shanuka.salesforce_android_api.clients.Constants;
import com.example.shanuka.salesforce_android_api.clients.UploadAttachmentDocument;
import com.example.shanuka.salesforce_android_api.dto.DocumentDto;
import com.example.shanuka.salesforce_android_api.model.Authentication;
import com.example.shanuka.salesforce_android_api.model.FileCreate;
import com.google.inject.Inject;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.retry.RetryPolicy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_document_upload)
public class DocumentUploadActivity extends BaseActivity {

    @InjectExtra(MainActivity.EXTRA_AUTHNTICATION)
    Authentication mAuthentication;

    @InjectView(R.id.imageView)
    ImageView imageView;

    @InjectView(R.id.buttonUpload)
    Button buttonUpload;

    @Inject
    DocumentDto mDocumentDto;

    protected static final int REQUEST_CAMERA = 0;
    protected static final int SELECT_FILE = 1;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ln.e(mAuthentication.getAuthentication());

        imageView.setImageBitmap(getBitmapFromAssets("image/index.jpg"));

        mDocumentDto.setCropX("0");
        mDocumentDto.setCropY("0");
        mDocumentDto.setCropSize("200");
//        mDocumentDto.setName("Marketing Brochure Q1");
//        mDocumentDto.setKeywords("marketing,sales,update");
//
//        mDocumentDto.setFolderId("005D0000001GiU7");
//        mDocumentDto.setType("pdf");



        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.hardware.camera"};

                int permsRequestCode = 200;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(perms, permsRequestCode);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){

        switch(permsRequestCode){

            case 200:

                boolean writeAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                selectImage();
                break;

        }

    }

    public Bitmap getBitmapFromAssets(String fileName) {
        AssetManager assetManager = getAssets();

        InputStream istr = null;
        try {
            istr = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);

        return bitmap;
    }

    public void performRequest(File file) {
        // showProgressFragment(R.id.progress);
        UploadAttachmentDocument request = new UploadAttachmentDocument(DocumentUploadActivity.this, mDocumentDto, file, mAuthentication);
        request.setRetryPolicy(new RetryPolicy() {

            @Override
            public void retry(SpiceException e) {
                // TODO Auto-generated method stub

            }

            @Override
            public int getRetryCount() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public long getDelayBeforeRetry() {
                // TODO Auto-generated method stub
                return 36000;
            }
        });
        spiceManager.execute(request, "sss",
                DurationInMillis.ALWAYS_EXPIRED, new RequestListener<FileCreate>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Ln.e(spiceException.getCause());

                    }

                    @Override
                    public void onRequestSuccess(FileCreate mFIleCreate) {
                        //Ln.e(mFIleCreate.getSuccess());

                        if (mFIleCreate.getSuccess()) {

                            Toast.makeText(DocumentUploadActivity.this, "File ID" + mFIleCreate.getId(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(DocumentUploadActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + "/temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    Bitmap bm;
    // private Bitmap outBit;
    // private Uri fileUri;
    private Uri fileUri;
    private String captureImagePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                File f = new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + "/temp.jpg");
                try {

                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    btmapOptions.inSampleSize = 1;
                    this.bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

//                    ExifInterface ei = new ExifInterface(f.getAbsolutePath());
//                    int orientation = ei.getAttributeInt(
//                            ExifInterface.TAG_ORIENTATION,
//                            ExifInterface.ORIENTATION_NORMAL);
//
//                    switch (orientation) {
//                        case ExifInterface.ORIENTATION_ROTATE_90:
//                            this.bm = RotateBitmap(this.bm, 90f);
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:
//                            this.bm = RotateBitmap(this.bm, 180f);
//                            break;
//                        // etc.
//                    }

                    f.delete();
                    OutputStream fOut = null;
                    File file = getOutputMediaFile();

                    try {
                        fOut = new FileOutputStream(file);
                        this.bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                        imageView.setImageBitmap(this.bm);
                        System.out.println("file " + file.getAbsolutePath());
                        // imageViewProfile.setVisibility(View.VISIBLE);
                        // circularImage.setVisibility(View.GONE);

                        performRequest(file);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, DocumentUploadActivity.this);

                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                btmapOptions.inSampleSize = 8;

                this.bm = BitmapFactory.decodeFile(tempPath, btmapOptions);

                // progressBarProfile.setVisibility(View.VISIBLE);
                // circularImage.setVisibility(View.GONE);

                //performRequest(tempPath);
                // ivImage.setImageBitmap(bm);
                // System.out.println("tempPath " + tempPath);
            }
        }
    }





    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.IMAGE_DIRECTORY_NAME);
        captureImagePath = mediaStorageDir.getPath();

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("ssss", "Oops! Failed create "
                        + Constants.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getHeight(),
                source.getWidth(), matrix, true);
    }

}