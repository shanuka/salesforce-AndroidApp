package com.example.shanuka.salesforce_android_api.dto;

import com.example.shanuka.salesforce_android_api.clients.Constants;

import org.springframework.util.LinkedMultiValueMap;

import roboguice.inject.ContextSingleton;

/**
 * Created by shanuka on 12/8/15.
 */
@ContextSingleton
public class AuthenticationDto {

    private String username;

    private String password;

    public LinkedMultiValueMap<String, String> getRequestBody() {

        LinkedMultiValueMap<String, String> loginServiceParam = new LinkedMultiValueMap<String, String>();
        loginServiceParam.add("client_id", Constants.client_id);
        loginServiceParam.add("client_secret", Constants.client_secret);

        loginServiceParam.add("grant_type", Constants.grant_type);
        loginServiceParam.add("username",getUsername());
        loginServiceParam.add("password", getPassword());

        return loginServiceParam;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
