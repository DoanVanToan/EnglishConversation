package com.framgia.englishconversation.screen.conversationdetail;

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
import com.framgia.englishconversation.databinding.ItemConversationDetailLeftBinding;
import com.framgia.englishconversation.databinding.ItemConversationDetailRightBinding;

import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 12/27/17.
 * Description:
 */

public class ConversationDetailAdapter extends
        BaseRecyclerViewAdapter<ConversationDetailAdapter.BaseConversationViewHolder> {

    private List<ConversationModel> mConversationModels;
    private ConversationDetailViewModel mViewModel;

    public ConversationDetailAdapter(@NonNull Context context,
                                     List<ConversationModel> conversationModels,
                                     ConversationDetailViewModel viewModel) {
        super(context);
        mConversationModels = conversationModels;
        mViewModel = viewModel;
    }

    @Override
    public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GravityType.LEFT:
                ItemConversationDetailLeftBinding leftBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(parent.getContext()),
                                R.layout.item_conversation_detail_left,
                                parent,
                                false);
                return new LeftConversationDetailViewHolder(leftBinding.getRoot());
            case GravityType.RIGHT:
                ItemConversationDetailRightBinding rightBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(parent.getContext()),
                                R.layout.item_conversation_detail_right,
                                parent,
                                false);
                return new RightConversationDetailViewHolder(rightBinding.getRoot());
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseConversationViewHolder holder, int position) {
        holder.bindView(mConversationModels.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mConversationModels.get(position).getGravity();
    }

    @Override
    public int getItemCount() {
        return mConversationModels.size();
    }

    /**
     * display left item in conversation list
     */
    class LeftConversationDetailViewHolder extends BaseConversationViewHolder {

        private ItemConversationDetailLeftBinding mBinding;

        LeftConversationDetailViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        void bindView(ConversationModel conversationModel) {
            mBinding.setConversation(conversationModel);
            mBinding.setViewModel(mViewModel);
            mBinding.setPosition(getAdapterPosition());
            mBinding.executePendingBindings();
        }
    }

    /**
     * display right item in conversation list
     */
    class RightConversationDetailViewHolder extends BaseConversationViewHolder {

        private ItemConversationDetailRightBinding mBinding;

        RightConversationDetailViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        void bindView(ConversationModel conversationModel) {
            mBinding.setConversation(conversationModel);
            mBinding.setViewModel(mViewModel);
            mBinding.setPosition(getAdapterPosition());
            mBinding.executePendingBindings();
        }
    }

    /**
     * base class for ViewHolder of ConversationDetailAdapter
     */
    abstract class BaseConversationViewHolder extends RecyclerView.ViewHolder {

        BaseConversationViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bindView(ConversationModel conversationModel);
    }
}
