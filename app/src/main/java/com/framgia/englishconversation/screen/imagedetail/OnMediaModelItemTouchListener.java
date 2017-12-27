package com.framgia.englishconversation.screen.imagedetail;

/**
 * Created by VinhTL on 21/12/2017.
 */

/**
 * OnRecyclerViewItemClickListener
 *
 * @param <T> Data from item click
 */
public interface OnMediaModelItemTouchListener<T> {
    void onTouchListener(T item, int postion);
}
