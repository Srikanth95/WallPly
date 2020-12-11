package com.wallply.wallply.utilities;

import android.content.Context;

/**
 * Created by sree on 9/17/2017.
 */

public class CloudinaryCredentials {

    private static CloudinaryCredentials instance;
    Context context;


    public String cloudName;
    public String cloudinaryUsername;
    public String cloudinaryPassword;
    public String cloudinaryUrlPrefix;
    public String cloudinaryAllImagesURL;
    public String cloudinaryAllFoldersURL;
    public String cloudinarySquareTr;
    public String cloudinaryLandscapeTr;
    public String cloudinaryPortraitTr;

    private CloudinaryCredentials(Context context) {
        this.context = context;
    }

    public synchronized static CloudinaryCredentials getInstance(Context context) {
        if (instance == null) {
            instance = new CloudinaryCredentials(context);
        }
        return instance;
    }

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public String getCloudinaryUsername() {
        return cloudinaryUsername;
    }

    public void setCloudinaryUsername(String cloudinaryUsername) {
        this.cloudinaryUsername = cloudinaryUsername;
    }

    public String getCloudinaryPassword() {
        return cloudinaryPassword;
    }

    public void setCloudinaryPassword(String cloudinaryPassword) {
        this.cloudinaryPassword = cloudinaryPassword;
    }

    public String getCloudinaryUrlPrefix() {
        return cloudinaryUrlPrefix;
    }

    public void setCloudinaryUrlPrefix(String cloudinaryUrlPrefix) {
        this.cloudinaryUrlPrefix = cloudinaryUrlPrefix;
    }

    public String getCloudinaryAllImagesURL() {
        return cloudinaryAllImagesURL;
    }

    public void setCloudinaryAllImagesURL(String cloudinaryAllImagesURL) {
        this.cloudinaryAllImagesURL = cloudinaryAllImagesURL;
    }

    public String getCloudinaryAllFoldersURL() {
        return cloudinaryAllFoldersURL;
    }

    public void setCloudinaryAllFoldersURL(String cloudinaryAllFoldersURL) {
        this.cloudinaryAllFoldersURL = cloudinaryAllFoldersURL;
    }

    public String getCloudinarySquareTr() {
        return cloudinarySquareTr;
    }

    public void setCloudinarySquareTr(String cloudinarySquareTr) {
        this.cloudinarySquareTr = cloudinarySquareTr;
    }

    public String getCloudinaryLandscapeTr() {
        return cloudinaryLandscapeTr;
    }

    public void setCloudinaryLandscapeTr(String cloudinaryLandscapeTr) {
        this.cloudinaryLandscapeTr = cloudinaryLandscapeTr;
    }

    public String getCloudinaryPortraitTr() {
        return cloudinaryPortraitTr;
    }

    public void setCloudinaryPortraitTr(String cloudinaryPortraitTr) {
        this.cloudinaryPortraitTr = cloudinaryPortraitTr;
    }
}
