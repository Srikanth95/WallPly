package com.wallply.wallply.fragments;


import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wallply.wallply.activities.BaseActivity;
import com.wallply.wallply.R;
import com.wallply.wallply.adapters.CollectionsAdapter;
import com.wallply.wallply.models.CategoryModel;

import java.util.ArrayList;


public class CollectionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CollectionsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

   // int[] data = {R.drawable.nature,R.drawable.city,R.drawable.illustration_amoled_p01,R.drawable.patterns,R.drawable.color,R.drawable.amoled,R.drawable.graphics_p03,R.drawable.gradient,R.drawable.material,R.drawable.cars};
  //  String[] names = {"Nature","City","Illustrations","Patterns","Colorful","For Amoled","Graphics","Gradients","Material","Cars"};

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

    public CollectionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recv1);
        recyclerView.setHasFixedSize(true);
        adapter = new CollectionsAdapter(getContext(),
                (ArrayList<CategoryModel>) BaseActivity.Companion.getWallpaperDatabase().dbAccess().getCategories());
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layoutManager);

        return v;
    }

}
