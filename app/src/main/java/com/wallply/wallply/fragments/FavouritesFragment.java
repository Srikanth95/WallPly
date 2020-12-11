package com.wallply.wallply.fragments;



import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wallply.wallply.R;
import com.wallply.wallply.activities.BaseActivity;
import com.wallply.wallply.adapters.FavouritesAdapter;
import com.wallply.wallply.models.WallpaperModel;

import java.util.ArrayList;


public class FavouritesFragment extends Fragment {
    private RecyclerView recyclerView;
    private FavouritesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;



    private BroadcastReceiver emptyListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            noText.setVisibility(View.VISIBLE);
        }
    };


    TextView noText;
    FrameLayout frameLayout;

    private static final String BUNDLE_RECYCLER_LAYOUT = "collections.recycler.layout";

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


    public FavouritesFragment() {
        // Required empty public constructor
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(BaseActivity.Companion.getWallpaperDatabase().dbAccess().getFavouriteArrayList() != null) {
            if (BaseActivity.Companion.getWallpaperDatabase().dbAccess().getFavouriteArrayList().size() > 0) {
                if (noText != null)
                    noText.setVisibility(View.GONE);
            }
            else if (noText != null)
                noText.setVisibility(View.VISIBLE);
        } else {
            if (noText != null)
            noText.setVisibility(View.VISIBLE);
        }
//        if(adapter != null)
//        adapter.reloadFavouritesList();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        if (adapter!= null)
//            adapter.reloadFavouritesList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favourite, container, false);

        noText = (TextView) v.findViewById(R.id.no_text);
        noText.setVisibility(View.GONE);


        recyclerView = (RecyclerView) v.findViewById(R.id.recv1);
        recyclerView.setHasFixedSize(true);
        if(BaseActivity.Companion.getWallpaperDatabase().dbAccess().getFavouriteArrayList() != null) {
            if (BaseActivity.Companion.getWallpaperDatabase().dbAccess().getFavouriteArrayList().size() > 0) {
                if (noText != null)
                    noText.setVisibility(View.GONE);
            }
            else if (noText != null)
                noText.setVisibility(View.VISIBLE);
        } else {
            if (noText != null)
                noText.setVisibility(View.VISIBLE);
        }
        adapter = new FavouritesAdapter(getContext(),
                (ArrayList<WallpaperModel>) BaseActivity.Companion.getWallpaperDatabase().dbAccess().getFavouriteArrayList());
        adapter.setHasStableIds(true);

        recyclerView.setAdapter(adapter);


        layoutManager = new GridLayoutManager(getContext(), 2){
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
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


        frameLayout = (FrameLayout) v.findViewById(R.id.frame);





        return v;
    }






    @Override
    public void onResume() {

        super.onResume();



        if (adapter!= null)
            adapter.notifyDataSetChanged();

        getContext().registerReceiver(emptyListReceiver, new IntentFilter("FAVOURITES_LIST_EMPTY"));

    }

    @Override
    public void onStop() {


        super.onStop();

        getContext().unregisterReceiver(emptyListReceiver);
    }

    @Override
    public void onPause() {


        super.onPause();
    }





    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}