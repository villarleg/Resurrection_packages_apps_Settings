/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.development;

import android.content.Context;
import android.os.SystemProperties;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

import com.android.settings.core.PreferenceController;
import com.android.settings.R;

public class TelephonyMonitorPreferenceController extends PreferenceController {

    private static final String KEY_TELEPHONY_MONITOR_SWITCH = "telephony_monitor_switch";
    static final String BUILD_TYPE = "ro.build.type";
    static final String PROPERTY_TELEPHONY_MONITOR = "persist.radio.enable_tel_mon";

    private SwitchPreference mPreference;

    public TelephonyMonitorPreferenceController(Context context) {
        super(context);
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        if (isAvailable()) {
            mPreference = (SwitchPreference) screen.findPreference(KEY_TELEPHONY_MONITOR_SWITCH);
            mPreference.setChecked(SystemProperties.getBoolean(PROPERTY_TELEPHONY_MONITOR, false));
        }
    }

    @Override
    public String getPreferenceKey() {
        return KEY_TELEPHONY_MONITOR_SWITCH;
    }

    @Override
    public boolean isAvailable() {
        return mContext.getResources().getBoolean(R.bool.config_show_telephony_monitor) &&
                (SystemProperties.get(BUILD_TYPE).equals("userdebug") ||
                        SystemProperties.get(BUILD_TYPE).equals("eng"));
    }

    @Override
    public void updateState(Preference preference) {
        updatePreference();
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (KEY_TELEPHONY_MONITOR_SWITCH.equals(preference.getKey())) {
            final SwitchPreference switchPreference = (SwitchPreference) preference;
            SystemProperties.set(PROPERTY_TELEPHONY_MONITOR,
                    switchPreference.isChecked() ? "true" : "false");
            Toast.makeText(mContext, R.string.telephony_monitor_toast,
                    Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    public void enablePreference(boolean enabled) {
        if (isAvailable()) {
            mPreference.setEnabled(enabled);
        }
    }

    public boolean updatePreference() {
        if (!isAvailable()) {
            return false;
        }
        final boolean enabled = SystemProperties.getBoolean(PROPERTY_TELEPHONY_MONITOR, false);
        mPreference.setChecked(enabled);
        return enabled;
    }

}