package com.wallply.wallply.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wallply.wallply.R;
import com.wallply.wallply.adapters.CategoryAdapter;


public class CategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    int[] data = {R.drawable.nature,R.drawable.city,R.drawable.patterns,R.drawable.color,R.drawable.amoled,R.drawable.gradient,R.drawable.material,R.drawable.cars};
    String[] names = {"Nature","City","Patterns","Colorful","For Amoled","Gradients","Material","Cars"};



    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recv1);
        recyclerView.setHasFixedSize(true);
        adapter = new CategoryAdapter(getContext(),data,names);
        recyclerView.setAdapter(adapter);
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        return v;
    }

}
