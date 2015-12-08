package com.example.shanuka.salesforce_android_api.dto;

import com.example.shanuka.salesforce_android_api.clients.Constants;

import org.springframework.util.LinkedMultiValueMap;

import roboguice.inject.ContextSingleton;

/**
 * Created by shanuka on 12/8/15.
 */
@ContextSingleton
public class DocumentDto {

    private String Name;

    private String Description;

    private String Type;

    private String FolderId;

    private String Keywords;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getFolderId() {
        return FolderId;
    }

    public void setFolderId(String folderId) {
        FolderId = folderId;
    }

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(String keywords) {
        Keywords = keywords;
    }
}
