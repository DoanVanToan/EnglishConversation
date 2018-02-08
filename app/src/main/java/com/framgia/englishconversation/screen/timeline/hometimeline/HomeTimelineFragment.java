package com.framgia.englishconversation.screen.timeline.hometimeline;

import com.framgia.englishconversation.data.source.SettingRepository;
import com.framgia.englishconversation.data.source.local.setting.SettingLocalDataSource;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsImpl;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.screen.timeline.TimelineContract;
import com.framgia.englishconversation.screen.timeline.TimelineFragment;

/**
 * Created by doan.van.toan on 2/8/18.
 */

public class HomeTimelineFragment extends TimelineFragment {

    public static TimelineFragment newInstance() {
        return new HomeTimelineFragment();

    }

    @Override
    protected TimelineContract.Presenter getPresenter(TimelineContract.ViewModel viewModel) {
        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        TimelineRepository timelineRepository =
                new TimelineRepository(new TimelineRemoteDataSource());
        SettingRepository settingRepository = new SettingRepository(
                new SettingLocalDataSource(new SharedPrefsImpl(getContext())));

        TimelineContract.Presenter presenter =
                new HomeTimelinePresenter(
                        viewModel,
                        repository,
                        timelineRepository,
                        settingRepository);

        return presenter;
    }
}
