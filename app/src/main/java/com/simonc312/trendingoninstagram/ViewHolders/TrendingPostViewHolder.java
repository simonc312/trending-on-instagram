package com.simonc312.trendingoninstagram.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.simonc312.trendingoninstagram.Adapters.TrendingAdapter;
import com.simonc312.trendingoninstagram.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Simon on 1/26/2016.
 */
public class TrendingPostViewHolder extends GridViewHolder {

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

    public TrendingPostViewHolder(View itemView,TrendingAdapter.PostItemListener listener) {
        super(itemView,listener);
        ButterKnife.bind(this, itemView);
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

    public void setProfileImage(String src){
        setImageHelper(src, iv_profile);
    }

    public void setCaption(String caption) {
        tv_caption.setText(caption);
    }
}