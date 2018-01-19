package com.framgia.englishconversation.data.source.local.setting;

import com.framgia.englishconversation.data.model.Setting;
import com.framgia.englishconversation.data.source.SettingDataSource;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsApi;
import com.framgia.englishconversation.data.source.local.sharedprf.SharedPrefsKey;

/**
 * Created by doan.van.toan on 1/16/18.
 */

public class SettingLocalDataSource implements SettingDataSource {
    private SharedPrefsApi mPrefsApi;

    public SettingLocalDataSource(SharedPrefsApi prefsApi) {
        mPrefsApi = prefsApi;
    }

    @Override
    public Setting getSetting() {
        Setting setting = new Setting();
        boolean isEnableAutoPlayed =
                mPrefsApi.get(SharedPrefsKey.PREF_SETTING_AUTO_PLAY, false);
        setting.setEnableAutoPlay(isEnableAutoPlayed);
        return setting;
    }

    @Override
    public void saveSetting(Setting setting) {
        if (setting == null) {
            return;
        }
        mPrefsApi.put(SharedPrefsKey.PREF_SETTING_AUTO_PLAY, setting.isEnableAutoPlay());
    }
}
