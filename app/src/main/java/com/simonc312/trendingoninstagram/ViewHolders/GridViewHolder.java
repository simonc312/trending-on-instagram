package com.simonc312.trendingoninstagram.ViewHolders;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.simonc312.trendingoninstagram.Adapters.TrendingAdapter;
import com.simonc312.trendingoninstagram.R;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Simon on 1/28/2016.
 */
public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @Bind(R.id.iv_item)
    ImageView iv_item;
    private final TrendingAdapter.PostItemListener listener;

    public GridViewHolder(View itemView,TrendingAdapter.PostItemListener listener){
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    public void setPostImage(String src) {
        setImageHelper(src, iv_item);
    }

    protected void setImageHelper(String src,ImageView image){
        Picasso.with(itemView.getContext())
                .load(src)
                .placeholder(R.color.placeholder_color)
                .into(image);
    }

    @Override
    public void onClick(View v) {
        listener.onPostClick(getAdapterPosition());
    }
}
