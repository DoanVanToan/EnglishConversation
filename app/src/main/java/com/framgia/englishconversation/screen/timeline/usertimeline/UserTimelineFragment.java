package com.framgia.englishconversation.screen.timeline.usertimeline;

import android.os.Bundle;

import com.framgia.englishconversation.data.model.UserModel;
import com.framgia.englishconversation.data.source.SettingRepository;
import com.framgia.englishconversation.data.source.local.setting.SettingLocalDataSource;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsImpl;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.screen.timeline.TimelineContract;
import com.framgia.englishconversation.screen.timeline.TimelineFragment;

import static com.framgia.englishconversation.utils.Constant.EXTRA_USER;

/**
 * Created by doan.van.toan on 2/8/18.
 */

public class UserTimelineFragment extends TimelineFragment {

    public static TimelineFragment newInstance(UserModel userModel) {
        TimelineFragment fragment = new UserTimelineFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_USER, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected TimelineContract.Presenter getPresenter(TimelineContract.ViewModel viewModel) {
        UserModel userModel = getArguments().getParcelable(EXTRA_USER);

        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        TimelineRepository timelineRepository =
                new TimelineRepository(new TimelineRemoteDataSource());
        SettingRepository settingRepository = new SettingRepository(
                new SettingLocalDataSource(new SharedPrefsImpl(getContext())));

        return new UserTimelinePresenter(viewModel, repository, timelineRepository,
                settingRepository, userModel);
    }
}
