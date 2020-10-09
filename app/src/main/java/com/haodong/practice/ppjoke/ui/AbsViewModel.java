package com.haodong.practice.ppjoke.ui;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * created by linghaoDo on 2020/9/13
 * description:
 * <p>
 * version:
 */
public abstract class AbsViewModel<T> extends ViewModel {
    protected DataSource dataSource;
    protected LiveData<PagedList<T>> pageData;
    protected MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();
    protected PagedList.Config config;

    public AbsViewModel() {
        config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(12) // setInitialLoadSizeHint>setPageSize 保证在页面数据加载完成之后，不立即加载第二页的数据
                .setEnablePlaceholders(false).build();
        pageData = new LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0) // 初始化参数，传递的key
                .setBoundaryCallback(callback)
                .build();

    }

    public LiveData<PagedList<T>> getPageData() {
        return pageData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public MutableLiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }


    PagedList.BoundaryCallback<T> callback = new PagedList.BoundaryCallback<T>() {
        @Override
        public void onZeroItemsLoaded() {
            boundaryPageData.postValue(false);
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            /*列表的第一条数据加载*/
            boundaryPageData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            /*列表的最后一条数据被加载*/
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };

    DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            dataSource = createDataSource();
            return dataSource;
        }
    };

    public abstract DataSource createDataSource();
}
