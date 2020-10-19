package com.haodong.practice.ppjoke.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haodong.practice.libcommon.extention.LiveDataBus;
import com.haodong.practice.ppjoke.databinding.LayoutFeedTypeImageBinding;
import com.haodong.practice.ppjoke.databinding.LayoutFeedTypeVideoBinding;
import com.haodong.practice.ppjoke.detail.FeedDetailActivity;
import com.haodong.practice.ppjoke.extention.AbsPagedListAdapter;
import com.haodong.practice.ppjoke.model.Feed;
import com.haodong.practice.ppjoke.ui.InteractionPresenter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by linghaoDo on 2020/9/13
 * description:
 * <p>
 * version:
 */
public class FeedAdapter extends AbsPagedListAdapter<Feed, FeedAdapter.ViewHolder> {
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
        return new ViewHolder(mViewDataBinding.getRoot(),mViewDataBinding);
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, viewType, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }


    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        final Feed feed = getItem(position);

        holder.bindData(feed);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedDetailActivity.startFeedDetailActivity(mContext, feed, mCategory);
                onStartFeedDetailActivity(feed);
                if (mFeedObserver == null) {
                    mFeedObserver = new FeedObserver();
                    LiveDataBus.get()
                            .with(InteractionPresenter.DATA_FROM_INTERACTION)
                            .observe((LifecycleOwner) mContext, mFeedObserver);
                }
                mFeedObserver.setFeed(feed);
            }
        });
    }
    public void onStartFeedDetailActivity(Feed feed) {

    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return feed.itemType;
    }

    private FeedObserver mFeedObserver;

    private class FeedObserver implements Observer<Feed> {
        private Feed mFeed;

        @Override
        public void onChanged(Feed feed) {
            if (mFeed.id != feed.id) {
                return;
            }
            mFeed.author = feed.author;
            mFeed.ugc = feed.ugc;
            mFeed.notifyChange();
        }

        public void setFeed(Feed mFeed) {
            this.mFeed = mFeed;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mBinding;

        public ViewHolder(@NonNull View itemView, ViewDataBinding binding) {
            super(itemView);
            mBinding = binding;
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
