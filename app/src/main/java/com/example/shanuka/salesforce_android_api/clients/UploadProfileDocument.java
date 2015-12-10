package com.example.shanuka.salesforce_android_api.clients;

import android.util.Log;

import com.example.shanuka.salesforce_android_api.DocumentUploadActivity;
import com.example.shanuka.salesforce_android_api.dto.DocumentDto;
import com.example.shanuka.salesforce_android_api.model.Authentication;
import com.example.shanuka.salesforce_android_api.model.FileCreate;
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

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


public class UploadProfileDocument extends SpringAndroidSpiceRequest<FileCreate> {

    public final DocumentUploadActivity ctx;
    private final Authentication muthentication;
    public String fileUrl;
    DocumentDto mDocumentDto;

    public UploadProfileDocument(DocumentUploadActivity fragmentActivity, DocumentDto mDocumentDto, String fileurl, Authentication muthentication) {
        super(FileCreate.class);
        this.mDocumentDto = mDocumentDto;
        this.fileUrl = fileurl;
        this.ctx = fragmentActivity;
        this.muthentication = muthentication;
    }

    @Override
    public FileCreate loadDataFromNetwork() throws Exception {
        String url = "https://ap2.salesforce.com/";
        String pathTemplate;

        pathTemplate = url
                + "services/data/v35.0/connect/user-profiles/00528000000iGwtAAE/photo";
        ObjectMapper mapper = new ObjectMapper();
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();


        // Log.e("mDocumentDto", "getFolderId " + this.mDocumentDto.getFolderId());
        HttpHeaders xHeader = new HttpHeaders();
        xHeader.add("Content-Disposition", "form-data; name=\"json\"");
        final Map<String, String> parameterMap = new HashMap<String, String>(4);
        parameterMap.put("charset", "UTF-8");
        xHeader.setContentType(
                new MediaType("application", "json", parameterMap));
        HttpEntity<String> xPart = new HttpEntity<>("{\"cropX\" : 0,\"cropY\" : 0,\"cropSize\" : 150 }", xHeader);
        parts.add("json", xPart);

        //System.out.println("edit cpd data " + mapper.writeValueAsString(this.mDocumentDto));


//        FileSystemResource fileSystemResource = new FileSystemResource(fileUrl) {
//
//            @Override
//            public String getFilename() {
//                //String filename = fFilename;
//
////                        if (fContentType.toLowerCase().equals("image/jpeg")) {
////                            if (!filename.toLowerCase().endsWith(".jpeg")) {
////                                filename += ".jpeg";
////                            }
////                        }
//
//                return "index.jpg";
//            }
//        };

        FileSystemResource mFileSystemResource = new FileSystemResource(fileUrl);
//
//
//        HttpHeaders headers = new HttpHeaders();
//        //headers.add("Content-Disposition", "form-data; name=\"fileUpload\"; filename=\"index.jpg\"");
//       // headers.setContentType(new MediaType("application","pdf"));
//        final Map<String, String> parameterMapf = new HashMap<String, String>(4);
//        parameterMapf.put("charset", "ISO-8859-1");
//        headers.setContentType(
//                new MediaType("application","octet-stream", parameterMapf));
//        headers.setContentDispositionFormData("fileUpload","index.jpg");

        HttpEntity httpEntity = new HttpEntity(mFileSystemResource);
        parts.add("fileUpload", httpEntity);


//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
//            System.setProperty("http.keepAlive", "false");
//        }


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
        httpHeaders.add("Authorization", muthentication.getAuthentication());
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(
                parts, httpHeaders);


        try {

            return getRestTemplate().postForObject(pathTemplate, request, FileCreate.class);
        } catch (HttpClientErrorException e2) {


            Log.d("ee", e2.getResponseBodyAsString());
            // throw new SpiceException(mLoginError.getErrorDescription());


            throw e2;
        }

    }
}
