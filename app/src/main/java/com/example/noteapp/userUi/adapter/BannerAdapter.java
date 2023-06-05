package com.example.noteapp.userUi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.noteapp.R;
import com.example.noteapp.model.News;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class BannerAdapter extends SliderViewAdapter<BannerAdapter.Holder> {
    final List<News> bannerList;
    final Context context;

    public BannerAdapter(List<News> bannerList, Context context) {
        this.bannerList = bannerList;
        this.context = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        News banner = bannerList.get(position);
        Glide.with(viewHolder.imageView).load(banner.getImg())
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }


    public class Holder extends SliderViewAdapter.ViewHolder{
        final ImageView imageView;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_banner);
        }
    }

}
