package com.example.shanuka.salesforce_android_api.clients;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.shanuka.salesforce_android_api.DocumentUploadActivity;
import com.example.shanuka.salesforce_android_api.dto.DocumentDto;
import com.example.shanuka.salesforce_android_api.model.Authentication;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.nio.charset.Charset;


public class UploadFileDocument extends SpringAndroidSpiceRequest<String> {

    public final DocumentUploadActivity ctx;
    private final Authentication muthentication;
    public String fileUrl;
    DocumentDto mDocumentDto;

    public UploadFileDocument(DocumentUploadActivity fragmentActivity, DocumentDto mDocumentDto, String fileurl, Authentication muthentication ) {
        super(String.class);
        this.mDocumentDto = mDocumentDto;
        this.fileUrl = fileurl;
        this.ctx = fragmentActivity;
        this.muthentication = muthentication;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        String url = "https://ap2.salesforce.com/";
        String pathTemplate;

        pathTemplate = url
                + "services/data/v35.0/sobjects/Document";
        ObjectMapper mapper = new ObjectMapper();
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();




        Log.e("mDocumentDto", "getFolderId " + this.mDocumentDto.getFolderId());
        HttpHeaders xHeader = new HttpHeaders();
        xHeader.add("Content-Disposition", "form-data; name=entity_document");
        xHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> xPart = new HttpEntity<>(mapper.writeValueAsString(this.mDocumentDto), xHeader);
        parts.add("entity_document", xPart);

        System.out.println("edit cpd data " + mapper.writeValueAsString(this.mDocumentDto));


        FileSystemResource fileSystemResource = new FileSystemResource(fileUrl) {

            @Override
            public String getFilename() {
                //String filename = fFilename;

//                        if (fContentType.toLowerCase().equals("image/jpeg")) {
//                            if (!filename.toLowerCase().endsWith(".jpeg")) {
//                                filename += ".jpeg";
//                            }
//                        }

                return "test.pdp";
            }
        };


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "form-data; name=\"Body\"; filename=\"2011Q1MktgBrochure.pdf\"");
        headers.setContentType(new MediaType("application","pdf"));
        HttpEntity httpEntity = new HttpEntity(fileSystemResource, headers);
        parts.add("Body", httpEntity);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }


        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();

        formHttpMessageConverter.setCharset(Charset.forName("UTF8"));
        getRestTemplate().getMessageConverters().add(
                formHttpMessageConverter);
        getRestTemplate().getMessageConverters().add(
                new ByteArrayHttpMessageConverter());

        getRestTemplate().getMessageConverters().add(
                new StringHttpMessageConverter());
        getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        //getRestTemplate().setMessageConverters().;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization",muthentication.getAuthentication());
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
                parts, httpHeaders);




        try {

            return getRestTemplate().postForObject(pathTemplate, request, String.class);
        } catch (HttpClientErrorException e2) {


            //
            try {

                final String mLoginError = mapper.readValue(
                        e2.getResponseBodyAsString(), String.class);
                ctx.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(ctx.getApplicationContext(),
                                ""+ mLoginError, Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                // throw new SpiceException(mLoginError.getErrorDescription());

            } catch (JsonGenerationException e) {

                e.printStackTrace();

            } catch (JsonMappingException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }
            throw e2;
        }

    }
}
