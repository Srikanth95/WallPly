package com.wallply.wallply.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DbAccess {

    @Insert
    void insertOnlySingleWallpaper (WallpaperModel wallpaperModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultipleWallpapers (ArrayList<WallpaperModel> wallpaperModelArrayList);

    @Query("SELECT * FROM WallpaperModel WHERE public_id = :public_id")
    WallpaperModel fetchOneWallpaperbyPublicId (int public_id);

    @Query("SELECT * FROM WallpaperModel")
    List<WallpaperModel> getWallpapersList();

    @Query("UPDATE WallpaperModel SET isFavourite = :isFavourite WHERE name = :name")
    void setFavourite(String name, boolean isFavourite);

    @Query("UPDATE WallpaperModel SET favouriteCount = :favouriteCount WHERE name = :name")
    void setFavouriteCount(String name, int favouriteCount);

    @Query("SELECT favouriteCount FROM WallpaperModel WHERE public_id = :public_id")
    int getFavouriteCount(String public_id);

    @Query("SELECT * FROM WallpaperModel WHERE isFavourite = 1")
    List<WallpaperModel> getFavouriteArrayList();

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertFavouriteID(FavouriteID favouriteID);
//
//    @Delete
//    void deleteFavouriteID(String name);
//
//    @Query("SELECT * FROM FavouriteID")
//    List<FavouriteID> getFavouriteIDList();


    @Update
    void updateWallpaper (WallpaperModel wallpaperModel);

    @Delete
    void deleteWallpaper (WallpaperModel wallpaperModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCategories(ArrayList<CategoryModel> categoryModelArrayList);

    @Query("SELECT * FROM CATEGORYMODEL")
    public List<CategoryModel> getCategories();

}

