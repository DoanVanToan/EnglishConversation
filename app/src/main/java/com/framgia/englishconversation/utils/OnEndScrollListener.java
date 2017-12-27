package com.framgia.englishconversation.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by VinhTL on 15/11/2017.
 */

public class OnEndScrollListener extends RecyclerView.OnScrollListener {
    private OnEndScroll mOnEndScroll;
    private AtomicBoolean isFetchingData;

    public OnEndScrollListener(OnEndScroll onEndScroll) {
        mOnEndScroll = onEndScroll;
        isFetchingData = new AtomicBoolean(false);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int lastVisibleItem =
                    ((LinearLayoutManager) recyclerView.getLayoutManager())
                            .findLastVisibleItemPosition();
            if (totalItemCount <= lastVisibleItem + 1 && !isFetchingData.get()) {
                isFetchingData.set(true);
                mOnEndScroll.onEndScrolled();
            }
        }
    }

    public interface OnEndScroll {
        void onEndScrolled();
    }

    public void setIsFetchingData(boolean isFetching) {
        isFetchingData.set(isFetching);
    }
}
