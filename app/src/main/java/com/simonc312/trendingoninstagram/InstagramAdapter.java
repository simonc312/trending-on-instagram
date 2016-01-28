package com.simonc312.trendingoninstagram;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by Simon on 1/26/2016.
 */
public class InstagramAdapter extends RecyclerView.Adapter<InstagramViewHolder> {
    private List<InstagramPostData> imageList;

    @Override
    public InstagramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new InstagramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InstagramViewHolder holder, int position) {
        InstagramPostData data = imageList.get(position);
        holder.setImage(data.getImageSource());
        holder.setUsername(data.getUsername());
        holder.setLikes(data.getLikeCount());
        holder.setTimePosted(data.getTimePosted());
        holder.setCaption(data.getCaption());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

}
