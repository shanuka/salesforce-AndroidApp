package com.example.shanuka.salesforce_android_api.clients;


import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        //Log.i("Authenti", "AuthenticationInterceptor " + mIAppreference.getToken());

        request.getHeaders().add("accept", "application/json");

        //request.getHeaders().add("Authorization", mIAppreference.getToken());

        // HttpHeaders httpHeaders = DigitalisApi.getHttpAuthorizationHeader();

//        String authuraisation = DigitalisApi.getAuthuraisation();
//        request.getHeaders().add("Authorization", authuraisation);
//
//        Log.i("Authorization", authuraisation);

        ClientHttpResponse response = execution.execute(request,
                body);

        return response;
    }
}