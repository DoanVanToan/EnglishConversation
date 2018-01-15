package com.framgia.englishconversation.screen.timeline;

/**
 * Created by VinhTL on 21/12/2017.
 */

public interface OnTimelineItemTouchListener<T> {
    void onItemTimelineClick(T item);

    void onItemUserNameClick(T item);
}
