/*
 * Copyright (C) 2016 The Pure Nexus Project
 * used for Nitrogen OS
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

package com.lessaosp.settings;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Surface;
import androidx.preference.Preference;
import com.android.settings.R;
import com.lessaosp.settings.preferences.Utils;

import com.android.settings.SettingsPreferenceFragment;

public class LessaospSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.lessaosp_settings);

        final String KEY_DEVICE_PART = "device_part";
        boolean packageInstalled = false;

        // DeviceParts
        String[] targetPackage = getResources().getStringArray(R.array.targetPackage);
        String[] targetClass = getResources().getStringArray(R.array.targetClass);
        Intent intentPref = getPreferenceScreen().findPreference(KEY_DEVICE_PART).getIntent();
        for (int i=0; i < targetPackage.length; i++)
        {
            if (Utils.isPackageInstalled(getActivity(), targetPackage[i])) {
                packageInstalled = true;
                intentPref.setClassName(targetPackage[i], targetClass[i]);
            }
        }
        if (!packageInstalled) getPreferenceScreen().removePreference(findPreference(KEY_DEVICE_PART));
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.LESS_SETTINGS;
    }

    public static void lockCurrentOrientation(Activity activity) {
        int currentRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        int frozenRotation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        switch (currentRotation) {
            case Surface.ROTATION_0:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                break;
            case Surface.ROTATION_90:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                break;
            case Surface.ROTATION_180:
                frozenRotation = orientation == Configuration.ORIENTATION_LANDSCAPE
                        ? ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                break;
            case Surface.ROTATION_270:
                frozenRotation = orientation == Configuration.ORIENTATION_PORTRAIT
                        ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        : ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                break;
        }
        activity.setRequestedOrientation(frozenRotation);
    }
}
