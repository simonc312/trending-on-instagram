package com.simonc312.trendingoninstagram.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simonc312.trendingoninstagram.Fragments.SearchFragment.InteractionListener;
import com.simonc312.trendingoninstagram.Models.SearchTag;
import com.simonc312.trendingoninstagram.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SearchTag} and makes a call to the
 * specified {@link InteractionListener}.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<SearchTag> mValues;
    private final InteractionListener mListener;

    public SearchAdapter(List<SearchTag> items, InteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SearchTag tag = mValues.get(position);
        holder.mItem = tag;
        holder.mIdView.setText(tag.getDisplayName());
        holder.mContentView.setText(tag.getDisplayTotalPosts());
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
        public final TextView mContentView;
        public SearchTag mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            mListener.onListFragmentInteraction(mItem.getName());
        }
    }
}
