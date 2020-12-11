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

import com.wallply.wallply.R;
import com.wallply.wallply.activities.CategoryViewActivityK;

/**
 * Created by sree on 2/27/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Context context;
    int[] data;
    String[] names;

    public CategoryAdapter(Context context, int[] data, String[] names) {
        this.context = context;
        this.data = data;
        this.names = names;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_image, parent, false);
        CategoryAdapter.ViewHolder vh = new CategoryAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        final int mid = data[position];
        final String sid = names[position];
        holder.textView.setText(sid);

        Glide.with(context).load(mid)


                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), CategoryViewActivityK.class);
                intent.putExtra("name", sid);
                v.getContext().startActivity(intent);
            }


        });


    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}


