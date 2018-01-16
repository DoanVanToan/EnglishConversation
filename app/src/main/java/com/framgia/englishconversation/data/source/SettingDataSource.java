package com.framgia.englishconversation.data.source;

import com.framgia.englishconversation.data.model.Setting;

/**
 * Created by doan.van.toan on 1/16/18.
 */

public interface SettingDataSource {
    Setting getSetting();

    void saveSetting(Setting setting);
}
