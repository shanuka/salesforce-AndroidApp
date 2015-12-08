package com.example.shanuka.salesforce_android_api.clients;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.inject.Singleton;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author Shanuka Gayashan
 */
@Singleton //a single instance of Foo is now used though the whole app
public class LoggerInterceptor implements ClientHttpRequestInterceptor {

    private static final String TAG = "Request";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        if (Constants.DEBUG_STATE) {
            Log.d(TAG, "Request getQuery : " + request.getURI());
            System.out.println("Body " + new String(body));
        }

        request.getHeaders().add("accept", "application/json");


        ClientHttpResponse response = execution.execute(request,
                body);

        return response;
    }
}