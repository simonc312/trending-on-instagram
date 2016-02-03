package com.simonc312.trendingoninstagram.helpers;

import android.content.Context;
import android.widget.ImageView;

import com.simonc312.trendingoninstagram.R;
import com.squareup.picasso.Picasso;

/**
 * Wrapper for Picasso or any third part image loading library
 * Created by Simon on 2/2/2016.
 */
public class ImageLoaderHelper {

    private ImageLoaderHelper(){
    }

    public static void load(Context context, String src,ImageView image){
        loadWithPlaceholder(context,src,image,R.color.placeholder_color);
    }

    public static  void loadWithPlaceholder(Context context, String src,ImageView image, int placeHolderId){
        Picasso.with(context)
                .load(src)
                .placeholder(placeHolderId)
                .into(image);
    }
}
