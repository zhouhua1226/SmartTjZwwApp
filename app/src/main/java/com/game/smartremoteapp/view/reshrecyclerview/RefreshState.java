package com.game.smartremoteapp.view.reshrecyclerview;

/**
 *  Created by mi on 2018/81/22.
 * 下拉刷新的状态
 */
public enum RefreshState {
    /**
     * 刷新之前的状态
     */
    STATE_NORMAL,
    /**
     * 释放刷新
     */
    STATE_RELEASE_TO_REFRESH,
    /**
     * 刷新中
     */
    STATE_REFRESHING,
    /**
     * 刷新完成
     */
    STATE_DONE
}
