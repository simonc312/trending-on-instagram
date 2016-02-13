package com.simonc312.trendingoninstagram.viewHolders;

import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.simonc312.trendingoninstagram.adapters.TrendingAdapter;
import com.simonc312.trendingoninstagram.helpers.ImageLoaderHelper;
import com.simonc312.trendingoninstagram.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Simon on 1/26/2016.
 */
public class TrendingPostViewHolder extends GridViewHolder {
    private Linkify.TransformFilter linkTransformer;
    @BindString(R.string.profile_scheme) String PROFILE_SCHEME;
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
    static Pattern usernamePattern;

    public TrendingPostViewHolder(View itemView,TrendingAdapter.PostItemListener listener) {
        super(itemView, listener);
        ButterKnife.bind(this, itemView);
        usernamePattern = Pattern.compile("^@(\\S+)");
        linkTransformer = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return String.format("%s/profile/?id=%s&name=%s",url,"3",match.group());
            }
        };
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
        ImageLoaderHelper.loadWithPlaceholder(itemView.getContext(), src, iv_profile, R.drawable.image_placeholder);
    }

    public void setCaption(String caption) {
        tv_caption.setText(caption);
    }

    public void linkifyUsername(){
        Linkify.addLinks(tv_username,usernamePattern,PROFILE_SCHEME+"://",null,linkTransformer);
    }
}