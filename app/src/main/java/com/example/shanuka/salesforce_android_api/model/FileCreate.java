package com.example.shanuka.salesforce_android_api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanuka on 12/8/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileCreate implements Parcelable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("errors")
    private List<String> errors;

    @JsonProperty("success")
    private Boolean success;

    public FileCreate() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    protected FileCreate(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0x01) {
            errors = new ArrayList<String>();
            in.readList(errors, String.class.getClassLoader());
        } else {
            errors = null;
        }
        byte successVal = in.readByte();
        success = successVal == 0x02 ? null : successVal != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        if (errors == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(errors);
        }
        if (success == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (success ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FileCreate> CREATOR = new Parcelable.Creator<FileCreate>() {
        @Override
        public FileCreate createFromParcel(Parcel in) {
            return new FileCreate(in);
        }

        @Override
        public FileCreate[] newArray(int size) {
            return new FileCreate[size];
        }
    };
}