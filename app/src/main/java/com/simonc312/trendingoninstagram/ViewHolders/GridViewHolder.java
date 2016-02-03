package com.simonc312.trendingoninstagram.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.simonc312.trendingoninstagram.adapters.TrendingAdapter;
import com.simonc312.trendingoninstagram.helpers.ImageLoaderHelper;
import com.simonc312.trendingoninstagram.R;

import butterknife.Bind;
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
        ImageLoaderHelper.load(image.getContext(),src,image);
    }

    @Override
    public void onClick(View v) {
        listener.onPostClick(getAdapterPosition());
    }
}
