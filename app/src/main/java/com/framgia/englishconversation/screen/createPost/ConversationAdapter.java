package com.framgia.englishconversation.screen.createPost;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishconversation.BaseRecyclerViewAdapter;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.ConversationModel;
import com.framgia.englishconversation.data.model.GravityType;
import com.framgia.englishconversation.databinding.ItemLeftConversationBinding;
import com.framgia.englishconversation.databinding.ItemRightConventionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 12/7/17.
 * Description:
 */

public class ConversationAdapter extends
        BaseRecyclerViewAdapter<ConversationAdapter.BaseConversationViewHolder> {

    private static final int INITIAL_ITEM_COUNT = 3;
    private List<ConversationModel> mConversationModelList;
    private CreatePostViewModel mCreatePostViewModel;

    public ConversationAdapter(@NonNull Context context,
                               CreatePostViewModel createPostViewModel) {
        super(context);
        mCreatePostViewModel = createPostViewModel;
        initDefaultData();
    }

    @Override
    public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GravityType.LEFT:
                ItemLeftConversationBinding leftBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(parent.getContext()),
                                R.layout.item_left_conversation,
                                parent,
                                false
                        );
                return new LeftConversationViewHolder(leftBinding.getRoot());
            case GravityType.RIGHT:
                ItemRightConventionBinding rightBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(parent.getContext()),
                                R.layout.item_right_convention,
                                parent,
                                false
                        );
                return new RightConversationViewHolder(rightBinding.getRoot());
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseConversationViewHolder holder, int position) {
        holder.bindView(mConversationModelList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mConversationModelList.get(position).getGravity();
    }

    @Override
    public int getItemCount() {
        return mConversationModelList.size();
    }

    /**
     * Init data with 3 item.
     */
    private void initDefaultData() {
        mConversationModelList = new ArrayList<>();
        ConversationModel conversationModel = new ConversationModel();
        for (int i = 0; i < INITIAL_ITEM_COUNT; i++) {
            mConversationModelList.add(conversationModel);
        }
    }

    private void updateData(List<ConversationModel> conversationModelsList) {
        if (conversationModelsList == null) {
            return;
        }
        mConversationModelList.addAll(conversationModelsList);
        notifyDataSetChanged();
    }

    /**
     * The ViewModel to display conversation which has the left gravity.
     */
    public class LeftConversationViewHolder extends BaseConversationViewHolder {

        private ItemLeftConversationBinding mLeftBinding;

        LeftConversationViewHolder(View itemView) {
            super(itemView);
            mLeftBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bindView(ConversationModel conversationModel) {
            mLeftBinding.setViewModel(mCreatePostViewModel);
            mLeftBinding.setConversation(conversationModel);
            mLeftBinding.executePendingBindings();
        }
    }

    /**
     * The ViewModel to display conversation which has the right gravity.
     */
    public class RightConversationViewHolder extends BaseConversationViewHolder {

        private ItemRightConventionBinding mRightBinding;

        RightConversationViewHolder(View itemView) {
            super(itemView);
            mRightBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void bindView(ConversationModel conversationModel) {
            mRightBinding.setViewModel(mCreatePostViewModel);
            mRightBinding.setConversation(conversationModel);
            mRightBinding.executePendingBindings();
        }
    }

    abstract class BaseConversationViewHolder extends RecyclerView.ViewHolder {

        BaseConversationViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindView(ConversationModel conversationModel);

    }

}
