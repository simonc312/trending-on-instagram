package com.simonc312.trendingoninstagram.ViewPagers;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Base Adaptor used by View Pager to switch views.
 * Because this extends from {@link FragmentPagerAdapter} once fragment
 * loses visibility it will be retained in memory though view hierarchy will be refreshed.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    protected final List<Fragment> mFragments = new ArrayList<>();
    protected final List<String> mFragmentTitles = new ArrayList<>();
    protected final List<Drawable> mFragmentIcons = new ArrayList<>();


    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        FragmentManager.enableDebugLogging(true);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    public void addFragment(Fragment fragment, String title, Drawable drawableRes) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
        mFragmentIcons.add(drawableRes);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //if icons not provided just use String titles
        if(mFragmentIcons.size() != mFragments.size())
            return mFragmentTitles.get(position);
        else
            return getPageWithImageAndTitle(position);
    }

    public Drawable getIcon(int position) {
        return mFragmentIcons.get(position);
    }

    public void setIcon(int position, Drawable icon){
        mFragmentIcons.set(position,icon);
    }

    private SpannableString getPageWithImageAndTitle(int position){
        Drawable image = mFragmentIcons.get(position);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        //SpannableString sb = new SpannableString("   " + mFragmentTitles.get(position));
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}

