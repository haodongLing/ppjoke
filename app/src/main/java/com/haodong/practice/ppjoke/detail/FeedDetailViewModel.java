package com.haodong.practice.ppjoke.detail;

import com.alibaba.fastjson.TypeReference;
import com.haodong.practice.lib_network.ApiResponse;
import com.haodong.practice.lib_network.ApiService;
import com.haodong.practice.ppjoke.login.UserManager;
import com.haodong.practice.ppjoke.model.Comment;
import com.haodong.practice.ppjoke.ui.AbsViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;

/**
 * created by linghaoDo on 2020/10/12
 * description:
 * <p>
 * version:
 */
public class FeedDetailViewModel extends AbsViewModel<Comment> {
    private long itemId;

    @Override
    public DataSource createDataSource() {
        return new DataSource();
    }
    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    class DataSource extends ItemKeyedDataSource<Integer, Comment> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Comment> callback) {
            /*加载页面的初始化数据*/
            loadData(params.requestedInitialKey, params.requestedLoadSize, callback);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Comment> callback) {
            if (params.key > 0) {
                loadData(params.key, params.requestedLoadSize, callback);
            }
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Comment> callback) {
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Comment item) {
            return item.id;

        }

        private void loadData(Integer key, int requestedLoadSize, LoadCallback<Comment> callback) {
            ApiResponse<List<Comment>> response = ApiService.get("/comment/queryFeedComments")
                    .addParam("id", key)
                    .addParam("itemId", itemId)
                    .addParam("userId", UserManager.get().getUserId())
                    .addParam("pageCount", requestedLoadSize)
                    .responseType(new TypeReference<ArrayList<Comment>>() {
                    }.getType())
                    .execute();

            List<Comment> list = response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(list);
        }

    }
}
