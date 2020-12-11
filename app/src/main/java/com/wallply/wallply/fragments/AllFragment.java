package com.wallply.wallply.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;

import com.wallply.wallply.activities.BaseActivity;
import com.wallply.wallply.adapters.WallpaperAdapter;


import com.wallply.wallply.R;
import com.wallply.wallply.models.WallpaperModel;

import java.util.ArrayList;


public class AllFragment extends Fragment {
    private RecyclerView recyclerView;
    public WallpaperAdapter adapter;
    private static final String BUNDLE_RECYCLER_LAYOUT = "allfragment.recycler.layout";
    private RecyclerView.LayoutManager layoutManager;


    IntentFilter change, progressIntent, loading, error, refresh;
    BroadcastReceiver loadingReceiver, errorReceiver, progressReceiver, adapterReceiver, refreshReceiver;




    TextView wait, check;
    Button button;
    ProgressBar progressBar;
    FrameLayout frameLayout;

    public RecyclerView.LayoutManager getLayoutManager(){
        return recyclerView.getLayoutManager();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    

    public AllFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_all, container, false);



//        AdView adView = v.findViewById(R.id.adView);
//
//        AdRequest request = new AdRequest.Builder()
//
//                .build();
//        adView.loadAd(request);






        recyclerView = (RecyclerView) v.findViewById(R.id.recv1);
        recyclerView.setHasFixedSize(true);
        adapter = new WallpaperAdapter(getContext(),
                (ArrayList<WallpaperModel>) BaseActivity.Companion.getWallpapers(),
                true);

        recyclerView.setAdapter(adapter);


        layoutManager = new GridLayoutManager(getContext(), 2);
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)){
                    case 0:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);




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
                checkInternetConnection();
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
        wait.setVisibility(View.GONE);
        check = (TextView) v.findViewById(R.id.checktext);
        check.setVisibility(View.GONE);
        button = (Button) v.findViewById(R.id.tryagain);
        button.setVisibility(View.GONE);
        frameLayout = (FrameLayout) v.findViewById(R.id.frame);
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkInternetConnection();
            }
        });


        checkInternetConnection();


        return v;
    }


    public void loading() {

        check.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
      //  wait.setVisibility(View.VISIBLE);
      //  progressBar.setVisibility(View.VISIBLE);


    }

    public void internetError() {

        wait.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
       // check.setVisibility(View.VISIBLE);
       // button.setVisibility(View.VISIBLE);

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

        if (adapter != null)
            adapter.notifyDataSetChanged();

        //recyclerView.setVerticalScrollbarPosition(0);


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

        if (isNetworkAvailable(getContext())) {

            loading();
        } else
            internetError();

    }

    public void checkInternetConnection() {
        if (!isNetworkAvailable(getContext())) {
            internetError();

        } else {

            loading();



        }
    }

    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}