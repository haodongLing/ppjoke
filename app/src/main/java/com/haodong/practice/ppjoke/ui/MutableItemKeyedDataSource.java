package com.haodong.practice.ppjoke.ui;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

/**
 * created by linghaoDo on 2020/10/19
 * description:
 * <p>
 * version:
 */
public abstract class MutableItemKeyedDataSource<Key, Value> extends ItemKeyedDataSource<Key, Value>  {
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Value> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Value> callback) {

    }

    @NonNull
    @Override
    public Key getKey(@NonNull Value item) {
        return null;
    }
}
