package com.framgia.englishconversation.screen.createPost;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.englishconversation.BaseRecyclerViewAdapter;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.model.ConversationModel;
import com.framgia.englishconversation.data.model.GravityType;
import com.framgia.englishconversation.databinding.ItemCreatingLeftConversationBinding;
import com.framgia.englishconversation.databinding.ItemCreatingRightConventionBinding;
import com.framgia.englishconversation.screen.basePost.BasePostViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fs-sournary.
 * Date on 12/7/17.
 * Description:
 */

public class ConversationAdapter extends
        BaseRecyclerViewAdapter<ConversationAdapter.BaseConversationViewHolder> {

    private static final int GRAVITY_AMOUNT = 2;
    private static final int INITIAL_ITEM_COUNT = 3;

    private List<ConversationModel> mData;
    private BasePostViewModel mBasePostViewModel;

    public ConversationAdapter(@NonNull Context context,
                               BasePostViewModel basePostViewModel) {
        super(context);
        mBasePostViewModel = basePostViewModel;
        initDefaultData();
    }

    @Override
    public BaseConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case GravityType.LEFT:
                ItemCreatingLeftConversationBinding leftBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(parent.getContext()),
                                R.layout.item_creating_left_conversation,
                                parent,
                                false);
                return new LeftConversationViewHolder(leftBinding);
            case GravityType.RIGHT:
                ItemCreatingRightConventionBinding rightBinding =
                        DataBindingUtil.inflate(
                                LayoutInflater.from(parent.getContext()),
                                R.layout.item_creating_right_convention,
                                parent,
                                false);
                return new RightConversationViewHolder(rightBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseConversationViewHolder holder, int position) {
        holder.bindView(mData.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getGravity();
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    /**
     * Init data with 3 item.
     */
    private void initDefaultData() {
        mData = new ArrayList<>();
        for (int i = 0; i < INITIAL_ITEM_COUNT; i++) {
            int gravity = (i % GRAVITY_AMOUNT == 0) ? GravityType.LEFT : GravityType.RIGHT;
            mData.add(new ConversationModel(gravity));
        }
    }

    public void setData(List<ConversationModel> conversationModels) {
        mData.clear();
        mData.addAll(conversationModels);
        notifyDataSetChanged();
    }

    public List<ConversationModel> getData() {
        return mData;
    }

    public boolean checkExistingConversations() {
        for (ConversationModel conversationModel : mData) {
            if (conversationModel.getContent() != null
                    || conversationModel.getMediaModel() != null) {
                return true;
            }
        }
        return false;
    }

    public List<ConversationModel> getValidatedData() {
        List<ConversationModel> result = new ArrayList<>();
        for (ConversationModel model : mData) {
            if (model.getMediaModel() != null || !TextUtils.isEmpty(model.getContent())) {
                result.add(model);
            }
        }
        return result;
    }

    public void addData(ConversationModel conversationModel) {
        if (conversationModel == null) {
            return;
        }
        mData.add(conversationModel);
        notifyItemInserted(mData.size());
    }

    public void changeGravity(ConversationModel conversationModel, int position) {
        int gravity = conversationModel.getGravity() == GravityType.LEFT
                ? GravityType.RIGHT : GravityType.LEFT;
        conversationModel.setGravity(gravity);
        notifyItemChanged(position);
    }

    public void deleteConversation(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    /**
     * The ViewModel to display conversation which has the left gravity.
     */
    public class LeftConversationViewHolder extends BaseConversationViewHolder {

        private ItemCreatingLeftConversationBinding mLeftBinding;

        LeftConversationViewHolder(ItemCreatingLeftConversationBinding binding) {
            super(binding.getRoot());
            mLeftBinding = binding;
        }

        @Override
        public void bindView(ConversationModel conversationModel) {
            mLeftBinding.setViewModel(mBasePostViewModel);
            mLeftBinding.setConversations(conversationModel);
            mLeftBinding.setPosition(getAdapterPosition());
            mLeftBinding.executePendingBindings();
        }
    }

    /**
     * The ViewModel to display conversation which has the right gravity.
     */
    public class RightConversationViewHolder extends BaseConversationViewHolder {

        private ItemCreatingRightConventionBinding mRightBinding;

        RightConversationViewHolder(ItemCreatingRightConventionBinding binding) {
            super(binding.getRoot());
            mRightBinding = binding;
        }

        @Override
        public void bindView(ConversationModel conversationModel) {
            mRightBinding.setViewModel(mBasePostViewModel);
            mRightBinding.setPosition(getAdapterPosition());
            mRightBinding.setConversations(conversationModel);
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