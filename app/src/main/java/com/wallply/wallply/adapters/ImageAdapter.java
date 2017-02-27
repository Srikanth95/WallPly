package com.wallply.wallply.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.quickblox.content.model.QBFile;
import com.wallply.wallply.FullscreenActivity;

import com.wallply.wallply.R;

import java.util.ArrayList;

/**
 * Created by sree on 2/27/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {


    private Context mContext;

    private ArrayList<QBFile> arrayList;


    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
        ImageAdapter.ViewHolder vh = new ImageAdapter.ViewHolder(v);

        return vh;
    }

    public ImageAdapter(Context context, ArrayList<QBFile> arrayList) {
        mContext = context;

        this.arrayList = arrayList;
    }

    public QBFile getItem(int position) {
        return arrayList.get(position);
    }


    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {


        final QBFile qbFile = getItem(position);
        final String sid = qbFile.getPublicUrl();


        Glide.with(mContext).load(sid).crossFade()


                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), FullscreenActivity.class);
                intent.putExtra("sid", sid);
                intent.putExtra("name", qbFile.getName().toString());
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

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView1);
        }
    }
}