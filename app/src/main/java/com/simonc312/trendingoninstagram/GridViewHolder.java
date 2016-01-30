package com.simonc312.trendingoninstagram;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Simon on 1/28/2016.
 */
public class GridViewHolder extends RecyclerView.ViewHolder{
    @Bind(R.id.iv_item)
    ImageView iv_item;

    public GridViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
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
}
