package com.example.shanuka.salesforce_android_api;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shanuka.salesforce_android_api.clients.AuthenticationRequest;
import com.example.shanuka.salesforce_android_api.clients.UploadFileDocument;
import com.example.shanuka.salesforce_android_api.dto.AuthenticationDto;
import com.example.shanuka.salesforce_android_api.dto.DocumentDto;
import com.example.shanuka.salesforce_android_api.model.Authentication;
import com.google.inject.Inject;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.retry.RetryPolicy;

import java.io.IOException;
import java.io.InputStream;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ln.e(mAuthentication.getAuthentication());

        imageView.setImageBitmap(getBitmapFromAssets("image/test.png"));

        mDocumentDto.setDescription("Marketing brochure for Q1 2011");
        mDocumentDto.setName("Marketing Brochure Q1");
        mDocumentDto.setKeywords("marketing,sales,update");

        mDocumentDto.setFolderId("005D0000001GiU7");
        mDocumentDto.setType("pdf");

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRequest();
            }
        });
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

    public void performRequest() {
        // showProgressFragment(R.id.progress);
        UploadFileDocument request = new UploadFileDocument(DocumentUploadActivity.this,mDocumentDto, "/storage/emulated/0/Download/api_rest.pdf",mAuthentication);
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
                DurationInMillis.ALWAYS_EXPIRED, new RequestListener<String>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
        Ln.e(spiceException.getCause());

                    }

                    @Override
                    public void onRequestSuccess(String s) {
                        Ln.e(s);
                    }
                });
    }
}