package com.example.shanuka.salesforce_android_api.clients;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.shanuka.salesforce_android_api.dto.AuthenticationDto;
import com.example.shanuka.salesforce_android_api.model.Authentication;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationRequest extends SpringAndroidSpiceRequest<Authentication> {

    private final AuthenticationDto mAuthenticationDto;

    public String key;

    public String offset = "0";

    public final Context ctx;

    private ErrorCalback errorCalback;


    public AuthenticationRequest(Context ctx, String key, AuthenticationDto mAuthenticationDto) {
        super(Authentication.class);
        this.key = key;

        this.ctx = ctx;

        this.mAuthenticationDto = mAuthenticationDto;

    }

    @Override
    public Authentication loadDataFromNetwork() throws Exception {


        String url = Constants.SERVER_URL;
        String pathTemplate;

        pathTemplate = url
                + "services/oauth2/token";

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }

        getRestTemplate().getMessageConverters().add(
                new FormHttpMessageConverter());

        getRestTemplate().getMessageConverters().add(
                new StringHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        LoggerInterceptor loggerInterceptor = new LoggerInterceptor();
        //interceptors.add(loggerInterceptor);
        getRestTemplate().setInterceptors(interceptors);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
                mAuthenticationDto.getRequestBody(), headers);

        try {
            System.out.println("mAuthenticationDto body " + mAuthenticationDto.getRequestBody());
            return getRestTemplate().postForObject(pathTemplate, request,
                    Authentication.class);
//            HttpHeaders requestHeaders = new HttpHeaders();
//            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
//            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(mAuthenticationDto.getRequestBody(), requestHeaders);
//            ResponseEntity<Authentication> response = getRestTemplate().exchange(pathTemplate, HttpMethod.POST, requestEntity, Authentication.class);
//            return response.getBody();
        } catch (HttpServerErrorException e) {

            if (Constants.DEBUG_STATE) {
                // Log.d(TAG, "authString authorization" + authString);
                Log.d("Exception", "body " + e.getResponseBodyAsString());
            }
            throw e;
        } catch (Exception e) {
            if (errorCalback != null) {
                errorCalback.onError(e);
            }
            throw e;

        }

    }

    public void setErrorCalback(ErrorCalback errorCalback) {
        this.errorCalback = errorCalback;
    }

    public interface ErrorCalback {
        void onError(Exception e);
    }

    public String createCacheKey() {
        return "item." + key;
    }
}
