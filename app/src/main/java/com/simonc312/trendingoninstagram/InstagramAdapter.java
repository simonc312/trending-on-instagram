package com.simonc312.trendingoninstagram;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Simon on 1/26/2016.
 */
public class InstagramAdapter extends RecyclerView.Adapter<GridViewHolder> implements View.OnClickListener{
    private Context mContext;
    private boolean isGridLayout;
    private List<InstagramPostData> postDataList;


    public InstagramAdapter(Context context, boolean isGridLayout){
        postDataList = new ArrayList<>();
        postDataList.add(new InstagramPostData());
        this.mContext = context;
        this.isGridLayout = isGridLayout;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutID = isGridLayout ? R.layout.rv_grid_item : R.layout.rv_item;
        View view = LayoutInflater.from(mContext).inflate(layoutID, parent, false);
        view.setOnClickListener(this);
        return isGridLayout ? new GridViewHolder(view) : new InstagramViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        InstagramPostData data = postDataList.get(position);
        holder.setPostImage(data.getImageSource());

        if(!isGridLayout){
            InstagramViewHolder instagramViewHolder = (InstagramViewHolder) holder;
            instagramViewHolder.setProfileImage(data.getProfileImageSource());
            instagramViewHolder.setUsername(data.getUsername());
            instagramViewHolder.setLikes(data.getDisplayLikeCount());
            instagramViewHolder.setTimePosted(data.getRelativeTimePosted());
            instagramViewHolder.setCaption(data.getCaption());
        }
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

    public void setIsGridLayout(boolean isGridLayout){
        this.isGridLayout = isGridLayout;
    }

    @Override
    public void onClick(View v) {
        if(isGridLayout) {
            Intent intent = new Intent(mContext.getString(R.string.action_layout_change));
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }
}
