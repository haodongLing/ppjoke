package com.haodong.practice.ppjoke.detail;

import android.view.LayoutInflater;
import android.view.View;

import com.haodong.practice.ppjoke.R;
import com.haodong.practice.ppjoke.databinding.ActivityFeedDetailTypeImageBinding;
import com.haodong.practice.ppjoke.databinding.LayoutFeedDetailTypeImageHeaderBinding;
import com.haodong.practice.ppjoke.databinding.LayoutFeedTypeImageBinding;
import com.haodong.practice.ppjoke.model.Feed;
import com.haodong.practice.ppjoke.view.PPImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created by linghaoDo on 2020/10/12
 * description:
 * <p>
 * version:
 */
public class ImageViewHandler extends ViewHandler {
    protected ActivityFeedDetailTypeImageBinding mImageBinding;

    protected LayoutFeedDetailTypeImageHeaderBinding mHeaderBinding;

    public ImageViewHandler(FragmentActivity activity) {
        super(activity);
        DataBindingUtil.setContentView(activity, R.layout.activity_feed_detail_type_image);
        mInateractionBinding = mImageBinding.interactionLayout;
        mRecyclerView = mImageBinding.recyclerView;
        mImageBinding.actionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    @Override
    public void bindInitData(Feed feed) {
        super.bindInitData(feed);
        mImageBinding.setFeed(mFeed);
        mHeaderBinding = LayoutFeedDetailTypeImageHeaderBinding.inflate(LayoutInflater.from(mActivity), mRecyclerView, false);
        mHeaderBinding.setFeed(mFeed);
        PPImageView headerImage=mHeaderBinding.headerImages;
        headerImage.bindData(mFeed.width,mFeed.height,mFeed.width>mFeed.height?0:16,mFeed.cover);
        listAdapter.addFooterView(mHeaderBinding.getRoot());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean visible = mHeaderBinding.getRoot().getTop() <= -mImageBinding.titleLayout.getMeasuredHeight();
                mImageBinding.authorInfoLayout.getRoot().setVisibility(visible ? View.VISIBLE : View.GONE);
                mImageBinding.title.setVisibility(visible ? View.GONE : View.VISIBLE);
            }
        });
        handleEmpty(false);
    }
}
