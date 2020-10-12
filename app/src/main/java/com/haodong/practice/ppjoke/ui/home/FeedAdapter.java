package com.haodong.practice.ppjoke.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haodong.practice.ppjoke.databinding.LayoutFeedTypeImageBinding;
import com.haodong.practice.ppjoke.databinding.LayoutFeedTypeVideoBinding;
import com.haodong.practice.ppjoke.detail.FeedDetailActivity;
import com.haodong.practice.ppjoke.model.Feed;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by linghaoDo on 2020/9/13
 * description:
 * <p>
 * version:
 */
public class FeedAdapter extends PagedListAdapter<Feed, FeedAdapter.ViewHolder> {
    ViewDataBinding mViewDataBinding = null;
    Context mContext;
    LayoutInflater mInflater;
    private String mCategory;


    protected FeedAdapter(Context context, String category) {
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCategory = category;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Feed.TYPE_IMAGE_TEXT) {
            mViewDataBinding = LayoutFeedTypeImageBinding.inflate(mInflater);

        } else {
            mViewDataBinding = LayoutFeedTypeVideoBinding.inflate(mInflater);

        }
        return new ViewHolder(mViewDataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedDetailActivity.startFeedDetailActivity(mContext, getItem(position), mCategory);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return feed.itemType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(Feed item) {
            if (mViewDataBinding instanceof LayoutFeedTypeImageBinding) {
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding) mViewDataBinding;
                imageBinding.setFeed(item);
                imageBinding.feedImage.bindData(item.width, item.height, 16, item.cover);
                imageBinding.setLifeCycleOwner((LifecycleOwner) mContext);
            } else {
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding) mViewDataBinding;
                videoBinding.setFeed(item);
                videoBinding.listPlayerView.bindData(mCategory, item.width, item.height, item.cover, item.url);
                videoBinding.setLifeCycleOwner((LifecycleOwner) mContext);
            }
        }
    }
}
