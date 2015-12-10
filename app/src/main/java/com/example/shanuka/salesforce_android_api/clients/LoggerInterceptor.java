package com.example.shanuka.salesforce_android_api.clients;

import com.google.inject.Singleton;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import roboguice.util.Ln;

/**
 * @author Shanuka Gayashan
 */
@Singleton //a single instance of Foo is now used though the whole app
public class LoggerInterceptor implements ClientHttpRequestInterceptor {

    private static final String TAG = "Request";


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        Ln.d("===========================request begin================================================");

        Ln.d("URI : " + request.getURI());
        Ln.d("Method : " + request.getMethod());
        Ln.d("Request Body : " + new String(body, "UTF-8"));
        Ln.d("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        Ln.d("============================response begin==========================================");
        Ln.d("status code: " + response.getStatusCode());
        Ln.d("status text: " + response.getStatusText());
        Ln.d("Response Body : " + inputStringBuilder.toString());
        Ln.d("=======================response end=================================================");
    }
}