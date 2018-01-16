package com.framgia.englishconversation.data.source;

import com.framgia.englishconversation.data.model.Setting;

/**
 * Created by doan.van.toan on 1/16/18.
 */

public class SettingRepository implements SettingDataSource {
    private SettingDataSource mLocalDataSource;

    public SettingRepository(SettingDataSource localDataSource) {
        mLocalDataSource = localDataSource;
    }

    @Override
    public Setting getSetting() {
        return mLocalDataSource.getSetting();
    }

    @Override
    public void saveSetting(Setting setting) {
        mLocalDataSource.saveSetting(setting);
    }
}
