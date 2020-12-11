package com.wallply.wallply.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.Snackbar;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.wallply.wallply.activities.BaseActivity;

import com.wallply.wallply.R;
import com.wallply.wallply.activities.FullscreenActivity;
import com.wallply.wallply.models.WallpaperModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.wallply.wallply.R.id.fullimage;
import static com.wallply.wallply.R.id.imageView;
import static com.wallply.wallply.R.id.title;

/**
 * Created by sree on 2/27/2017.
 */

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {


    private Context mContext;
    //private ArrayList<WallpaperModel> favouriteList;
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;
    boolean showAppTitle;
    View v;


    private ArrayList<WallpaperModel> arrayList;

    private ColorDrawable[] vibrantLightColorList =
            {
                    new ColorDrawable(Color.parseColor("#9ACCCD")), new ColorDrawable(Color.parseColor("#8FD8A0")),
                    new ColorDrawable(Color.parseColor("#CBD890")), new ColorDrawable(Color.parseColor("#DACC8F")),
                    new ColorDrawable(Color.parseColor("#D9A790")), new ColorDrawable(Color.parseColor("#D18FD9")),
                    new ColorDrawable(Color.parseColor("#FF6772")), new ColorDrawable(Color.parseColor("#DDFB5C"))
            };

    private ColorDrawable[] vibrantDarkColorList =
            {
                    new ColorDrawable(Color.parseColor("#7f0000")), new ColorDrawable(Color.parseColor("#00251a")),
                    new ColorDrawable(Color.parseColor("#560027")), new ColorDrawable(Color.parseColor("#ac1900")),
                    new ColorDrawable(Color.parseColor("#12005e")), new ColorDrawable(Color.parseColor("#1b0000")),
                    new ColorDrawable(Color.parseColor("#00363a")), new ColorDrawable(Color.parseColor("#000a12"))
            };


    public WallpaperAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WallpaperAdapter.ViewHolder vh;
        if(showAppTitle){
            switch (viewType){
                case 0:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_title, parent, false);
                    vh = new WallpaperAdapter.ViewHolder(v, viewType);
                    break;
                default:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
                    vh = new WallpaperAdapter.ViewHolder(v);
                    break;
            }
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
            vh = new WallpaperAdapter.ViewHolder(v);
        }

        vh.setIsRecyclable(false);
        return vh;


    }

    public ColorDrawable getRandomLightDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public ColorDrawable getRandomDarkDrawbleColor() {
        int idx = new Random().nextInt(vibrantDarkColorList.length);
        return vibrantDarkColorList[idx];
    }

    public WallpaperAdapter(Context context, ArrayList<WallpaperModel> arrayList, boolean showAppTitle) {
        mContext = context;
        this.arrayList = arrayList;
        //favouriteList = BaseActivity.Companion.getFavouriteWallpapers();
        coordinatorLayout = new CoordinatorLayout(mContext);
        this.showAppTitle = showAppTitle;

    }



    public WallpaperModel getItem(int position) {
        return arrayList.get(position);
    }


    @Override
    public void onBindViewHolder(final WallpaperAdapter.ViewHolder holder, final int position) {
        if(position != 0 || !showAppTitle) {

            final int newPosition;
            if(showAppTitle)
            newPosition = position - 1;
            else
                newPosition = position;


                final String publicId = arrayList.get(newPosition).getPublic_id();
                //final int itemPosition =  newPosition;
                final WallpaperModel wallpaperModel = arrayList.get(newPosition);
                // final String sid = qbFile.getPublicUrl();

                ColorDrawable colorDrawable = getRandomDarkDrawbleColor();
                Glide.with(mContext).load(getSquareUrl(wallpaperModel.getUrl()))
                        // .placeholder(colorDrawable)
                        .placeholder(R.color.colorPrimary)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(holder.imageView);

                // holder.textLayout.setBackground(colorDrawable);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(), FullscreenActivity.class);
                        intent.putExtra("name", arrayList.get(newPosition).getName());
                        intent.putExtra("image_url", getSquareUrl(wallpaperModel.getUrl()));
                        intent.putExtra("o_image_url", wallpaperModel.getUrl());
                        intent.putExtra("public_id", wallpaperModel.getPublic_id());
                        intent.putExtra("is_favourite", containsInFavourites(wallpaperModel.getName()));
                        //final View image = v.findViewById(R.id.imageView1);
                        //ViewCompat.setTransitionName(image, "wallpaper");
                        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) v.getContext(), holder.imageView, "wallpaper");
                        v.getContext().startActivity(intent);
                    }


                });


                holder.wallName.setText(arrayList.get(newPosition).getName());


                if (containsInFavourites(wallpaperModel.getName()))
                    holder.favourite.setImageResource(R.drawable.heart_fill);
                else
                    holder.favourite.setImageResource(R.drawable.round_favorite_border_24);
                holder.favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (containsInFavourites(wallpaperModel.getName())) {
                            BaseActivity.Companion.removeFromFavourites(wallpaperModel.getName());
                            mContext.sendBroadcast(new Intent("FAVOURITE_REMOVED"));
                            BaseActivity.Companion.getFavouriteWallpapers().remove(wallpaperModel);
                            //notifyDataSetChanged();
                            holder.favourite.setImageResource(R.drawable.round_favorite_border_24);
                        } else {
                            BaseActivity.Companion.addToFavourites(wallpaperModel);
                            //notifyDataSetChanged();
                            mContext.sendBroadcast(new Intent("FAVOURITE_ADDED"));
                            holder.favourite.setImageResource(R.drawable.round_favorite_24);
                        }

                    }
                });

        } else {
            holder.titleText.setText("Wallpapers");
        }
    }

    public boolean containsInFavourites(String name){
        if(BaseActivity.Companion.getFavouriteWallpapers() != null){
            for (int i = 0; i < BaseActivity.Companion.getFavouriteWallpapers().size(); i++){
                if(BaseActivity.Companion.getFavouriteWallpapers().get(i).getName().equals(name))
                    return true;
            }
        }
        return false;
    }

//    public void reloadFavouriteList() {
//        favouriteList = BaseActivity.Companion.getFavouriteWallpapers();
//        //notifyItemRemoved(position);
//        //notifyItemChanged(position);
//        notifyDataSetChanged();
//    }




    public String getSquareUrl(String url){
        String prefix = url.substring(0,url.indexOf("upload")+6);
        String postfix = url.substring(url.indexOf("upload")+6);
        String middle = BaseActivity.cloudinaryCredentials.cloudinarySquareTr;
      //  Log.i("Url","after adding transformation : "+prefix+middle+postfix);
        return prefix+middle+postfix;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        if(position == 0){
            viewType = 0;
        }
        return viewType;
    }

    @Override
    public int getItemCount() {
        return showAppTitle? arrayList.size()+1 : arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, favourite;
        TextView wallName;
        FrameLayout textLayout;
        TextView titleText;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);
            favourite = (ImageView) itemView.findViewById(R.id.favourite);
            wallName = (TextView) itemView.findViewById(R.id.wall_name);
            textLayout = (FrameLayout) itemView.findViewById(R.id.text_layout);

        }

        public ViewHolder(View v, int viewType) {
            super(v);
            titleText = v.findViewById(R.id.fragment_title);
        }
    }
}