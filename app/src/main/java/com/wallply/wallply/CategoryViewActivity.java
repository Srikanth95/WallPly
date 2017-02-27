package com.wallply.wallply;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;


import com.wallply.wallply.adapters.ImageAdapter;
import com.wallply.wallply.quickblox.DataHolder;

import com.quickblox.core.request.QBPagedRequestBuilder;


public class CategoryViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    BroadcastReceiver adapterReceiver;
    TextView tittle;
    Intent intent;
    String data;
    Toolbar toolbar;
    TextView wait, check;
    Button button;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    QBPagedRequestBuilder builder;
    CharSequence category;
    String cat;
    IntentFilter change;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView1);

        AdRequest request = new AdRequest.Builder()

                .build();
        adView.loadAd(request);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryViewActivity.super.onBackPressed();
            }
        });
        tittle = (TextView) findViewById(R.id.title);


        wait = (TextView) findViewById(R.id.waitText);
        check = (TextView) findViewById(R.id.checktext);
        button = (Button) findViewById(R.id.tryagain);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        check.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        adapterReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                adapter.notifyDataSetChanged();
            }
        };


        recyclerView = (RecyclerView) findViewById(R.id.recv1);
        intent = getIntent();
        Bundle b = intent.getExtras();
        data = b.getString("name");
        builder = new QBPagedRequestBuilder();


        recyclerView.setHasFixedSize(true);

        switch (data) {
            case "Gradients":


                tittle.setText("Gradients");

                category = "gradient";
                cat = "gradient";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("gradient"));


                break;
            case "Nature":

                tittle.setText("Nature");

                category = "nature";
                cat = "nature";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("nature"));

                break;
            case "City":

                tittle.setText("City");

                category = "city";
                cat = "city";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("city"));

                break;
            case "Material":

                tittle.setText("Material");

                category = "material";
                cat = "material";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("material"));

                break;
            case "Patterns":

                tittle.setText("Patterns");

                category = "pattern";
                cat = "pattern";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("pattern"));

                break;
            case "Colorful":

                tittle.setText("Colorful");

                category = "color";
                cat = "color";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("color"));

                break;
            case "For Amoled":

                tittle.setText("For Amoled");

                category = "amoled";
                cat = "amoled";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("amoled"));

                break;
            case "Cars":

                tittle.setText("Cars");

                category = "car";
                cat = "car";
                adapter = new ImageAdapter(this, DataHolder.getInstance().getQBFiles("car"));

                break;

            default:
                break;

        }


        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        frameLayout.setVisibility(View.INVISIBLE);

        change = new IntentFilter("com.wallply.wallply.CHANGE");
        this.registerReceiver(adapterReceiver, change);


    }


    private void updateData() {


        Intent intent = new Intent(getApplicationContext(), CategoryViewActivity.class);
        startActivity(intent);


    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onResume() {
        this.registerReceiver(adapterReceiver, change);

        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(adapterReceiver);
        super.onPause();
    }
}
