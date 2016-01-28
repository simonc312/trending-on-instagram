package com.simonc312.trendingoninstagram;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Simon on 1/26/2016.
 */
public class InstagramAdapter extends RecyclerView.Adapter<InstagramViewHolder> {
    private Context mContext;
    private List<InstagramPostData> postDataList;


    public InstagramAdapter(Context context){
        postDataList = new ArrayList<>();
        postDataList.add(new InstagramPostData());
        this.mContext = context;
    }

    @Override
    public InstagramViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false);
        return new InstagramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InstagramViewHolder holder, int position) {
        InstagramPostData data = postDataList.get(position);
        holder.setImage(data.getImageSource());
        holder.setUsername(data.getUsername());
        holder.setLikes(data.getLikeCount());
        holder.setTimePosted(data.getRelativeTimePosted());
        holder.setCaption(data.getCaption());
    }

    @Override
    public int getItemCount() {
        return postDataList.size();
    }

    public void addPost(InstagramPostData post){
        postDataList.add(0, post);
        notifyItemInserted(0);
    }

    public void addPosts(List<InstagramPostData> posts){
        postDataList.addAll(0, posts);
        notifyDataSetChanged();
    }

}
