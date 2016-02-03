package com.simonc312.trendingoninstagram.Adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simonc312.trendingoninstagram.Fragments.SearchFragment.InteractionListener;
import com.simonc312.trendingoninstagram.ImageLoaderHelper;
import com.simonc312.trendingoninstagram.Models.SearchTag;
import com.simonc312.trendingoninstagram.Models.UserTag;
import com.simonc312.trendingoninstagram.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SearchTag} and makes a call to the
 * specified {@link InteractionListener}.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static final int USER_TYPE = 1;
    private static final int TAG_TYPE = 0;
    private List<SearchTag> mValues;
    private final InteractionListener mListener;

    public SearchAdapter(List<SearchTag> items,InteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if(mValues.get(position) instanceof UserTag)
            type = USER_TYPE;
        else
            type = TAG_TYPE;
        return type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResourceId = (viewType == USER_TYPE) ? R.layout.fragment_search_user_item : R.layout.fragment_search_item;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SearchTag tag = mValues.get(position);
        holder.mItem = tag;
        String name;
        if(tag instanceof UserTag) {
            UserTag userTag = (UserTag) tag;
            ImageLoaderHelper.load(
                    holder.imageView.getContext(),
                    userTag.getProfilePicture(),
                    holder.imageView);
            name = userTag.getDisplayName();
        } else{
            name = tag.getDisplayName();
            holder.mContentView.setText(tag.getDisplayTotalPosts());
        }
        holder.mIdView.setText(name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addItem(SearchTag tag) {
        mValues.add(0,tag);
        notifyItemInserted(0);
    }

    public void update(List<SearchTag> newList) {
        mValues = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        @Nullable
        public final TextView mContentView;
        public final ImageView imageView;
        public SearchTag mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            imageView = (ImageView) view.findViewById(R.id.image);
            view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            mListener.onListFragmentInteraction(mItem.getName());
        }
    }
}
