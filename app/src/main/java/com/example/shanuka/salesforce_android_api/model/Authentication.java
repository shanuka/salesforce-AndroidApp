package com.example.shanuka.salesforce_android_api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by shanuka on 12/8/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authentication implements Parcelable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("issued_at")
    private String issuedAt;

    @JsonProperty("instance_url")
    private String instanceUrl;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("access_token")
    private String accessToken;

    public Authentication() {
    }

    public String getAuthentication(){
        return tokenType.concat(" ").concat(accessToken);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    protected Authentication(Parcel in) {
        id = in.readString();
        issuedAt = in.readString();
        instanceUrl = in.readString();
        tokenType = in.readString();
        signature = in.readString();
        accessToken = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(issuedAt);
        dest.writeString(instanceUrl);
        dest.writeString(tokenType);
        dest.writeString(signature);
        dest.writeString(accessToken);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Authentication> CREATOR = new Parcelable.Creator<Authentication>() {
        @Override
        public Authentication createFromParcel(Parcel in) {
            return new Authentication(in);
        }

        @Override
        public Authentication[] newArray(int size) {
            return new Authentication[size];
        }
    };
}
