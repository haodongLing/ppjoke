package com.haodong.practice.ppjoke.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.haodong.practice.libnavannotation.FragmentDestination;
import com.haodong.practice.ppjoke.exoplayer.PageListPlayDetector;
import com.haodong.practice.ppjoke.ui.AbsListFragment;
import com.haodong.practice.ppjoke.model.Feed;
import com.haodong.practice.ppjoke.ui.AbsViewModel;
import com.haodong.practice.ppjoke.ui.MutablePageKeyedDataSource;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
public class HomeFragment extends AbsListFragment<Feed, HomeViewModel> {
    private String feedType;
    private PageListPlayDetector playDetector;
    private boolean shouldPause = true;
    private HomeViewModel homeViewModel;

    @Override
    protected void afterCreateView() {
        mViewModel.getCacheLiveData().observe(this, new Observer<PagedList<Feed>>() {
            @Override
            public void onChanged(PagedList<Feed> feeds) {
                submitList(feeds);
            }
        });
    }

    public static HomeFragment newInstance(String feedType) {
        Bundle args = new Bundle();
        args.putString("feedType", feedType);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            Type argument = arguments[1];
            Class modelClazz = ((Class) argument).asSubclass(AbsViewModel.class);
            mViewModel = (HomeViewModel) ViewModelProviders.of(this).get(modelClazz);
            mViewModel.getPageData().observe(this, new Observer<PagedList<Feed>>() {
                @Override
                public void onChanged(PagedList<Feed> feeds) {
                    adapter.submitList(feeds);
                }
            });
            mViewModel.getBoundaryPageData().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    finishRefresh(aBoolean);
                }
            });
        }
        playDetector = new PageListPlayDetector(this, mRecyclerView);
        mViewModel.setFeedType(feedType);
    }

    @Override
    public PagedListAdapter getAdapter() {
        feedType = getArguments() == null ? "all" : getArguments().getString("feedType");
        return new FeedAdapter(getContext(), feedType);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Feed feed = adapter.getCurrentList().get(adapter.getItemCount() - 1);
        mViewModel.loadAfter(feed.id, new ItemKeyedDataSource.LoadCallback<Feed>() {
            @Override
            public void onResult(@NonNull List<Feed> data) {
                PagedList.Config config = adapter.getCurrentList().getConfig();
                if (data != null && data.size() > 0) {
                    MutablePageKeyedDataSource<Feed> dataSource = new MutablePageKeyedDataSource<>();
                    dataSource.data.addAll(data);
                    PagedList<Feed> pagedList = dataSource.buildNewPagedList(config);
                    submitList(pagedList);
                }
            }
        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            playDetector.onPause();
        } else {
            playDetector.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (shouldPause) {
            playDetector.onPause();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        shouldPause = true;
        //由于沙发Tab的几个子页面 复用了HomeFragment。
        //我们需要判断下 当前页面 它是否有ParentFragment.
        //当且仅当 它和它的ParentFragment均可见的时候，才能恢复视频播放
        if (getParentFragment() != null) { // 说明Fragment 嵌套 了 fragment
            if (getParentFragment().isVisible() && isVisible()) {
                Log.e("homefragment", "onResume: feedtype:" + feedType);
                playDetector.onResume();
            }
        } else {
            if (isVisible()) {
                Log.e("homefragment", "onResume: feedtype:" + feedType);
                playDetector.onResume();
            }
        }
    }



    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
        //详情见：LivePagedListBuilder#compute方法
        mViewModel.getDataSource().invalidate();
    }
}