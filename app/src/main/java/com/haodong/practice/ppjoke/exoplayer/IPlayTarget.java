package com.haodong.practice.ppjoke.exoplayer;

import android.view.ViewGroup;

/**
 * created by linghaoDo on 2020/9/11
 * description:
 * <p>
 * version:
 */
public interface IPlayTarget {
    ViewGroup getOwner();

    // 活跃状态，视频可播放
    void onActive();

    // 非活跃状态，暂停
    void inActive();

    boolean isPlaying();
}
