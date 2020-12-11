package com.wallply.wallply.adapters;

import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wallply.wallply.activities.BaseActivity;
import com.wallply.wallply.R;
import com.wallply.wallply.activities.CategoryViewActivityK;
import com.wallply.wallply.models.CategoryModel;
import com.wallply.wallply.models.WallpaperModel;

import java.util.ArrayList;


/**
 * Created by sree on 2/27/2017.
 */

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.ViewHolder> {
    Context context;
    ArrayList<CategoryModel> categoriesList;
    ArrayList<WallpaperModel> arrayList;

    public CollectionsAdapter(Context context, ArrayList<CategoryModel> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
        arrayList = new ArrayList<>(BaseActivity.Companion.getWallpapers());
    }

    @Override
    public CollectionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        CollectionsAdapter.ViewHolder vh;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_title, parent, false);
                vh = new CollectionsAdapter.ViewHolder(view, viewType);
                return vh;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
                vh = new CollectionsAdapter.ViewHolder(view);
                return vh;
        }


    }

    @Override
    public int getItemViewType(int position) {
        return position==0?0:1;
    }

    public String getLandscapeUrl(String url){
        String prefix = url.substring(0,url.indexOf("upload")+6);
        String postfix = url.substring(url.indexOf("upload")+6);
        String middle = BaseActivity.cloudinaryCredentials.cloudinaryLandscapeTr;
      //  Log.i("Url","after adding transformation : "+prefix+middle+postfix);
        return prefix+middle+postfix;
    }

    @Override
    public void onBindViewHolder(CollectionsAdapter.ViewHolder holder, int position) {

        if(position != 0) {

            int newPosition = position - 1;

            final String catName = categoriesList.get(newPosition).getCategoryName();
            holder.textView.setText(catName);
            String url = "";


            for (int i = 0; i < arrayList.size(); i++) {

                if (arrayList.get(i).getUrl().contains(catName)) {

                    url = getLandscapeUrl(arrayList.get(i).getUrl());
                    break;

                }

            }

            Glide.with(context).load(url)
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), CategoryViewActivityK.class);
                    intent.putExtra("name", catName);
                    v.getContext().startActivity(intent);
                }


            });

        } else {
            holder.titleView.setText("Collections");
        }


    }

    @Override
    public int getItemCount() {
        return categoriesList.size()+1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView titleView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }

        public ViewHolder(View view, int viewType) {
            super(view);
            titleView = view.findViewById(R.id.fragment_title);
        }
    }
}


