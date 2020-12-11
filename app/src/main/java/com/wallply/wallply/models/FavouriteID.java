package com.wallply.wallply.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavouriteID {
    @NonNull @PrimaryKey String wallpaperID;

    public FavouriteID(String wallpaperID){
        this.wallpaperID = wallpaperID;
    }
}
