package com.wallply.wallply.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.wallply.wallply.adapters.ImageAdapter;
import com.wallply.wallply.quickblox.DataHolder;


import com.wallply.wallply.R;
import com.wallply.wallply.quickblox.ServerSetup;


public class AllFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    IntentFilter change, progressIntent, loading, error, refresh;
    BroadcastReceiver loadingReceiver, errorReceiver, progressReceiver, adapterReceiver, refreshReceiver;


    QBPagedRequestBuilder builder;
    Intent serverIntent;


    TextView wait, check;
    Button button;
    ProgressBar progressBar;
    FrameLayout frameLayout;


    public AllFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all, container, false);

        NativeExpressAdView adView = (NativeExpressAdView) v.findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder()

                .build();
        adView.loadAd(request);


        serverIntent = new Intent(getContext(), ServerSetup.class);


        recyclerView = (RecyclerView) v.findViewById(R.id.recv1);
        recyclerView.setHasFixedSize(true);
        adapter = new ImageAdapter(getContext(), DataHolder.getInstance().getQBFiles());
        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        builder = new QBPagedRequestBuilder();


        loadingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                loading();

            }
        };

        errorReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                internetError();
            }
        };

        progressReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                progressCheck();
            }
        };

        adapterReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                adapter.notifyDataSetChanged();
            }
        };

        refreshReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refresh();
            }
        };


        loading = new IntentFilter("com.wallply.wallply.LOADING");
        getContext().registerReceiver(loadingReceiver, loading);
        error = new IntentFilter("com.wallply.wallply.ERROR");
        getContext().registerReceiver(errorReceiver, error);
        change = new IntentFilter("com.wallply.wallply.CHANGE");
        getContext().registerReceiver(adapterReceiver, change);
        progressIntent = new IntentFilter("com.wallply.wallply.PROGRESS");
        getContext().registerReceiver(progressReceiver, progressIntent);
        refresh = new IntentFilter("com.wallply.wallply.REFRESH");
        getContext().registerReceiver(refreshReceiver, refresh);


        wait = (TextView) v.findViewById(R.id.waitText);
        check = (TextView) v.findViewById(R.id.checktext);
        button = (Button) v.findViewById(R.id.tryagain);
        frameLayout = (FrameLayout) v.findViewById(R.id.frame);
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        check.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                refresh();
            }
        });


        checkInternetConnection();


        return v;
    }


    public void loading() {
        check.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        wait.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }

    public void internetError() {

        wait.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        check.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);

    }

    public void progressCheck() {
        if (progressBar.isIndeterminate()) {
            recyclerView.setVisibility(View.VISIBLE);

            frameLayout.setVisibility(View.INVISIBLE);


        }
    }

    @Override
    public void onResume() {

        getContext().registerReceiver(adapterReceiver, change);
        getContext().registerReceiver(progressReceiver, progressIntent);
        getContext().registerReceiver(loadingReceiver, loading);
        getContext().registerReceiver(errorReceiver, error);
        getContext().registerReceiver(refreshReceiver, refresh);


        super.onResume();
    }

    @Override
    public void onStop() {


        super.onStop();
    }

    @Override
    public void onPause() {

        getContext().unregisterReceiver(adapterReceiver);
        getContext().unregisterReceiver(progressReceiver);
        getContext().unregisterReceiver(loadingReceiver);
        getContext().unregisterReceiver(errorReceiver);
        getContext().unregisterReceiver(refreshReceiver);

        super.onPause();
    }

    public void refresh() {
        loading();
        if (isNetworkAvailable(getContext())) {

            DataHolder.getInstance().clear();
            getContext().stopService(serverIntent);
            getContext().startService(serverIntent);

        } else
            internetError();

    }

    public void checkInternetConnection() {
        if (!isNetworkAvailable(getContext())) {
            internetError();

        } else {
            loading();
            Intent intent = new Intent(getContext(), ServerSetup.class);
            getContext().startService(intent);
        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
