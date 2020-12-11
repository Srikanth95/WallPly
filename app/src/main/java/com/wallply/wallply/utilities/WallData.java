package com.wallply.wallply.utilities;

import android.content.Context;

import com.wallply.wallply.models.CategoryModel;
import com.wallply.wallply.models.WallpaperModel;

import java.util.ArrayList;

/**
 * Created by sree on 9/16/2017.
 */

public class WallData {

    public ArrayList<WallpaperModel> allImagesArrayList;
    public ArrayList<WallpaperModel> allImagesFavouriteArrayList;
    public ArrayList<CategoryModel> allCategoriesArrayList;
    private static WallData instance;
    public Context context;

    public ArrayList<CategoryModel> getAllCategoriesArrayList() {
        return allCategoriesArrayList;
    }

    public void setAllCategoriesArrayList(ArrayList<CategoryModel> allCategoriesArrayList) {
        this.allCategoriesArrayList = allCategoriesArrayList;
    }

    // Constructor
    private WallData(Context context) {
        this.context = context;
    }

    // Thread safe singleton
    public synchronized static WallData getInstance(Context context) {
        if (instance == null) {
            instance = new WallData(context);
        }
        return instance;
    }

    public ArrayList<WallpaperModel> getAllImagesArrayList() {
        return allImagesArrayList;
    }

    public ArrayList<WallpaperModel> getAllImagesFavouriteArrayList() {
        return allImagesFavouriteArrayList;
    }

    public void setAllImagesFavouriteArrayList(ArrayList<WallpaperModel> allImagesFavouriteArrayList) {
        this.allImagesFavouriteArrayList = allImagesFavouriteArrayList;
    }

    public void setAllImagesArrayList(ArrayList<WallpaperModel> allImagesArrayList) {
        this.allImagesArrayList = allImagesArrayList;

    }

}
