/*
 * SPDX-FileCopyrightText: 2019 Erik Duisters <e.duisters1@gmail.com>
 *
 * SPDX-License-Identifier: GPL-2.0-only OR GPL-3.0-only OR LicenseRef-KDE-Accepted-GPL
 */

package org.kde.kdeconnect.UserInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.annotation.Nullable;

public class DeviceSettingsAlertDialogFragment extends AlertDialogFragment {
    private static final String KEY_PLUGIN_KEY = "PluginKey";
    private static final String KEY_DEVICE_ID = "DeviceId";

    private String pluginKey;
    private String deviceId;

    public DeviceSettingsAlertDialogFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (args == null || !args.containsKey(KEY_PLUGIN_KEY)) {
            throw new RuntimeException("You must call Builder.setPluginKey() to set the plugin");
        }
        if (!args.containsKey(KEY_DEVICE_ID)) {
            throw new RuntimeException("You must call Builder.setDeviceId() to set the device");
        }

        pluginKey = args.getString(KEY_PLUGIN_KEY);
        deviceId = args.getString(KEY_DEVICE_ID);

        setCallback(new Callback() {
            @Override
            public void onPositiveButtonClicked() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        Intent intent = new Intent(requireActivity(), PluginSettingsActivity.class);
                        intent.putExtra(PluginSettingsActivity.EXTRA_DEVICE_ID, deviceId);
                        intent.putExtra(PluginSettingsActivity.EXTRA_PLUGIN_KEY, pluginKey);
                        requireActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    }
                }
            }
        });
    }

    public static class Builder extends AbstractBuilder<DeviceSettingsAlertDialogFragment.Builder, DeviceSettingsAlertDialogFragment> {
        @Override
        public DeviceSettingsAlertDialogFragment.Builder getThis() {
            return this;
        }

        public DeviceSettingsAlertDialogFragment.Builder setPluginKey(String pluginKey) {
            args.putString(KEY_PLUGIN_KEY, pluginKey);
            return getThis();
        }

        public DeviceSettingsAlertDialogFragment.Builder setDeviceId(String deviceId) {
            args.putString(KEY_DEVICE_ID, deviceId);
            return getThis();
        }

        @Override
        protected DeviceSettingsAlertDialogFragment createFragment() {
            return new DeviceSettingsAlertDialogFragment();
        }
    }
}
