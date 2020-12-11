package com.wallply.wallply.models;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WallpaperModel.class, CategoryModel.class, FavouriteID.class}, version = 1, exportSchema = false)
public abstract class WallpaperDatabase extends RoomDatabase {

    public abstract DbAccess dbAccess();
}
