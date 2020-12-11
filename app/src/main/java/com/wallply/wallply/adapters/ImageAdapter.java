package com.wallply.wallply.adapters;

import android.content.Context;
import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import com.wallply.wallply.R;
import com.wallply.wallply.activities.FullscreenActivity;

import java.util.ArrayList;

/**
 * Created by sree on 2/27/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


    private Context mContext;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    private ArrayList<String> arrayList;


    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
        ImageAdapter.ViewHolder vh = new ImageAdapter.ViewHolder(v);

        return vh;
    }

    public ImageAdapter(Context context, ArrayList<String> arrayList) {
        mContext = context;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        this.arrayList = arrayList;
    }

    public String getItem(int position) {
        return arrayList.get(position);
    }


    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {


        final String qbFile = getItem(position);
       // final String sid = qbFile.getPublicUrl();











        Glide.with(mContext).using(new FirebaseImageLoader()).load(storageReference.child("thumbnails").child(qbFile)).crossFade().diskCacheStrategy(DiskCacheStrategy.RESULT)


                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), FullscreenActivity.class);
               // intent.putExtra("sid", sid);
                intent.putExtra("name", qbFile);
                //intent.putExtra("public_id",wallpaperModel.getPublic_id());
                v.getContext().startActivity(intent);
            }


        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);

        }
    }
}