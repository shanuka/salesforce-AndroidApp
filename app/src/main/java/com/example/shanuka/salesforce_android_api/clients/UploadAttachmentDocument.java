package com.example.shanuka.salesforce_android_api.clients;

import android.os.Build;
import android.widget.Toast;

import com.example.shanuka.salesforce_android_api.DocumentUploadActivity;
import com.example.shanuka.salesforce_android_api.dto.DocumentDto;
import com.example.shanuka.salesforce_android_api.model.Authentication;
import com.example.shanuka.salesforce_android_api.model.FileCreate;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class UploadAttachmentDocument extends SpringAndroidSpiceRequest<FileCreate> {

    public final DocumentUploadActivity ctx;
    private final Authentication muthentication;
    public File file;
    DocumentDto mDocumentDto;

    public UploadAttachmentDocument(DocumentUploadActivity fragmentActivity, DocumentDto mDocumentDto, File fileurl, Authentication muthentication) {
        super(FileCreate.class);
        this.mDocumentDto = mDocumentDto;
        this.file = fileurl;
        this.ctx = fragmentActivity;
        this.muthentication = muthentication;
    }

    @Override
    public FileCreate loadDataFromNetwork() throws Exception {
        String url = "https://ap2.salesforce.com/";
        String pathTemplate;

        pathTemplate = url
                + "services/data/v35.0/sobjects/Attachment";
        ObjectMapper mapper = new ObjectMapper();
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();


        // Log.e("mDocumentDto", "getFolderId " + this.mDocumentDto.getFolderId());
        HttpHeaders xHeader = new HttpHeaders();
        xHeader.add("Content-Disposition", "form-data; name=json");
        xHeader.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> xPart = new HttpEntity<>("{\"ParentId\":\"00328000009wzlIAAQ\",\"Name\":\""+file.getName()+"\"}", xHeader);
        parts.add("json", xPart);

       // System.out.println("fileUrl  " + fileUrl);


        FileSystemResource fileSystemResource = new FileSystemResource(file.getAbsoluteFile()) {

            @Override
            public String getFilename() {
                //String filename = fFilename;

//                        if (fContentType.toLowerCase().equals("image/jpeg")) {
//                            if (!filename.toLowerCase().endsWith(".jpeg")) {
//                                filename += ".jpeg";
//                            }
//                        }

                return file.getName();
            }
        };


        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "form-data; name=\"Body\"; filename=\""+file.getName()+"\"");
        headers.setContentType(MediaType.IMAGE_JPEG);
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
        getRestTemplate().setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        //getRestTemplate().setMessageConverters().;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", muthentication.getAuthentication());
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
                parts, httpHeaders);
        // RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        LoggerInterceptor loggerInterceptor = new LoggerInterceptor();
        interceptors.add(loggerInterceptor);
        getRestTemplate().setInterceptors(interceptors);


        try {

            return getRestTemplate().postForObject(pathTemplate, request, FileCreate.class);
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
                                "" + mLoginError, Toast.LENGTH_SHORT)
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
