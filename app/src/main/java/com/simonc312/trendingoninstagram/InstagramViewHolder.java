package com.simonc312.trendingoninstagram;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Simon on 1/26/2016.
 */
class InstagramViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.iv_item)
    ImageView iv_image;
    @Bind(R.id.iv_profile)
    ImageView iv_profile;
    @Bind(R.id.tv_username)
    TextView tv_username;
    @Bind(R.id.tv_time_posted)
    TextView timePosted;
    @Bind(R.id.tv_likes)
    TextView tv_likes;
    @Bind(R.id.tv_caption)
    TextView tv_caption;

    public InstagramViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setLikes(String likes) {
        tv_likes.setText(likes);
    }

    public void setTimePosted(String time) {
        timePosted.setText(time);
    }

    public void setUsername(String username) {
        tv_username.setText(username);
    }

    public void setPostImage(String src) {
        setImageHelper(src, iv_image);
    }

    public void setProfileImage(String src){
        setImageHelper(src,iv_profile);
    }

    public void setCaption(String caption) {
        tv_caption.setText(caption);
    }

    private void setImageHelper(String src,ImageView image){
        Picasso.with(itemView.getContext())
                .load(src)
                .placeholder(android.R.drawable.btn_star)
                .into(image);
    }
}