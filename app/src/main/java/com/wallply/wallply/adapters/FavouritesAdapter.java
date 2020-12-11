package com.wallply.wallply.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.Snackbar;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.wallply.wallply.R;
import com.wallply.wallply.activities.BaseActivity;
import com.wallply.wallply.activities.FullscreenActivity;
import com.wallply.wallply.models.WallpaperModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * Created by sree on 2/27/2017.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {


    private Context mContext;
    CoordinatorLayout coordinatorLayout;
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


    public FavouritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FavouritesAdapter.ViewHolder vh;
        switch (viewType){
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_title, parent, false);
                vh = new FavouritesAdapter.ViewHolder(v, viewType);
                return vh;

            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
                vh = new FavouritesAdapter.ViewHolder(v);
                return vh;

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public ColorDrawable getRandomLightDrawbleColor() {
        int idx = new Random().nextInt(vibrantLightColorList.length);
        return vibrantLightColorList[idx];
    }

    public ColorDrawable getRandomDarkDrawbleColor() {
        int idx = new Random().nextInt(vibrantDarkColorList.length);
        return vibrantDarkColorList[idx];
    }

    public FavouritesAdapter(Context context, ArrayList<WallpaperModel> arrayList) {
        mContext = context;
        this.arrayList = arrayList;
        coordinatorLayout = new CoordinatorLayout(mContext);
    }



    public WallpaperModel getItem(int position) {
        return arrayList.get(position);
    }


    @Override
    public void onBindViewHolder(final FavouritesAdapter.ViewHolder holder, final int position) {

        if(position != 0 ) {

            final int newPosition = position - 1;

            final String publicId = arrayList.get(newPosition).getPublic_id();
            final WallpaperModel wallpaperModel = arrayList.get(newPosition);
            // final String sid = qbFile.getPublicUrl();

            ColorDrawable colorDrawable = getRandomDarkDrawbleColor();
            Glide.with(mContext).load(getSquareUrl(wallpaperModel.getUrl()))
                    // .placeholder(colorDrawable)
                    .into(holder.imageView);
            // holder.textLayout.setBackground(colorDrawable);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), FullscreenActivity.class);
                    intent.putExtra("name", wallpaperModel.getName());
                    intent.putExtra("image_url", wallpaperModel.getUrl());
                    intent.putExtra("o_image_url", wallpaperModel.getUrl());
                    intent.putExtra("public_id", wallpaperModel.getPublic_id());
                    intent.putExtra("is_favourite", true);
                    v.getContext().startActivity(intent);
                }


            });


            holder.wallName.setText(arrayList.get(newPosition).getName());


            //  if(containsInFavourites(publicId))
            holder.favourite.setImageResource(R.drawable.heart_fill);
            // else
            //  holder.favourite.setImageResource(R.drawable.heart);
            holder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //  if(containsInFavourites(publicId)) {

                    BaseActivity.Companion.removeFromFavourites(wallpaperModel.getName());
                    reloadFavouritesList(holder.getAdapterPosition(), newPosition);
                    //holder.favourite.setImageResource(R.drawable.heart);
                    // }
                    // else {
                    // addToFavourites(publicId);
                    // }

                }
            });

        } else{
            holder.titleText.setText("Favourites");
        }
    }

    public void reloadFavouritesList(int position, int arrayPosition){
        //notifyItemRemoved(position);

        arrayList.remove(position - 1);
        if (arrayList.size() == 0)
            mContext.sendBroadcast(new Intent("FAVOURITES_LIST_EMPTY"));
        BaseActivity.Companion.setFavouriteWallpapers(arrayList);
        notifyItemRemoved(position);

    }

    public String getSquareUrl(String url){
        String prefix = url.substring(0,url.indexOf("upload")+6);
        String postfix = url.substring(url.indexOf("upload")+6);
        String middle = BaseActivity.cloudinaryCredentials.cloudinarySquareTr;
       // Log.i("Url","after adding transformation : "+prefix+middle+postfix);
        return prefix+middle+postfix;
    }

    @Override
    public long getItemId(int position) {
        if(position > 0)
        return arrayList.get(position-1).getName().hashCode();

        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size()+1;
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