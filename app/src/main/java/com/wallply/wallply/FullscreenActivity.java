package com.wallply.wallply;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


import java.io.IOException;


import static com.wallply.wallply.R.layout.activity_fullscreen;


public class FullscreenActivity extends AppCompatActivity {
    ImageView imageView;
    Button b2, download;

    String sid, name;
    InterstitialAd mInterstitialAd;


    CoordinatorLayout coordinatorLayout;
    ProgressDialog progressDoalog;
    WallpaperManager wallpaperManager;


    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(activity_fullscreen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Toast.makeText(ctxt, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        };
        this.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8883586149598239/5406560502");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                onBackPressed();
            }
        });

        requestNewInterstitial();


        imageView = (ImageView) findViewById(R.id.fullimage);
        download = (Button) findViewById(R.id.download);

        b2 = (Button) findViewById(R.id.setWallpaper);
        Bundle bundle = getIntent().getExtras();
        sid = bundle.getString("sid");
        name = bundle.getString("name");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        Glide.with(this).load(sid).diskCacheStrategy(DiskCacheStrategy.ALL).
                into(imageView);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDoalog = new ProgressDialog(FullscreenActivity.this);
                progressDoalog.setMessage(" Downloading High Quality Image...");
                progressDoalog.setIndeterminate(true);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
                Glide.with(FullscreenActivity.this)
                        .load(sid)
                        .asBitmap().format(DecodeFormat.PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                                try {
                                    wallpaperManager.setBitmap(resource);
                                    progressDoalog.dismiss();

                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Successfully applied as your wallpaper", Toast.LENGTH_SHORT);

                                    toast.show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri url = Uri.parse(sid);
                downloadData(url, name);


            }
        });


    }


    public void downloadData(Uri uri, String fileName) {


        long downloadReference;

        // Create request for android download manager
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Toast.makeText(this, "Download Started...", Toast.LENGTH_SHORT).show();
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle(fileName);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();


        //Setting description of request


        //Set the local destination for the downloaded file to a path
        //within the application's external files directory

        request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES, "/WallPly/" + fileName);


        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);


    }


    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {


            super.onBackPressed();
        }
    }

    public boolean onSupportNavigateUp() {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            onBackPressed();
        }
        return true;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()

                .build();

        mInterstitialAd.loadAd(adRequest);
    }


}
