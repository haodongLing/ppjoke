package com.haodong.practice.ppjoke.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.haodong.practice.ppjoke.model.Feed;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * created by linghaoDo on 2020/10/10
 * description:
 * <p>
 * version:
 */
public class FeedDetailActivity  extends AppCompatActivity {
    private static final String KEY_FEED = "key_feed";
    public static final String KEY_CATEGORY = "key_category";
    private ViewHandler viewHandler = null;

    public static void startFeedDetailActivity(Context context, Feed item,String category){
        Intent intent = new Intent(context, FeedDetailActivity.class);
        intent.putExtra(KEY_FEED, item);
        intent.putExtra(KEY_CATEGORY, category);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Feed feed = (Feed) getIntent().getSerializableExtra(KEY_FEED);
        if (feed == null) {
            finish();
            return;
        }

        if (feed.itemType == Feed.TYPE_IMAGE_TEXT) {
            viewHandler = new ImageViewHandler(this);
        } else {
            viewHandler = new VideoViewHandler(this);
        }

        viewHandler.bindInitData(feed);
    }
}
