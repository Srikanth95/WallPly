package com.wallply.wallply

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.wallply.wallply.activities.BaseActivity
import com.wallply.wallply.utilities.WallData

class WallplyApplication: MultiDexApplication() {
    companion object{
        lateinit var usersSnapshot: DataSnapshot
        //lateinit var favouritesSnapshot: DataSnapshot
        lateinit var wallpapersSnapshot: DataSnapshot
        //lateinit var wallData: WallData
    }

    override fun onCreate() {
        super.onCreate()

        when(getSharedPreferences("WALL", MODE_PRIVATE).getInt("current_theme",0)){
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        //wallData = WallData.getInstance(this)

    }

    override fun attachBaseContext(base: Context?) {
        //MultiDex.install(this)
        super.attachBaseContext(base)
    }

}