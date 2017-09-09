/*
  BSD 2-Clause License

 Copyright (c) 2017, LoloDev
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lolodev.permissionswrapper.wrapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import lolodev.permissionswrapper.constants.Constants;
import lolodev.permissionswrapper.RequestPermissionsActv;
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;

/**
 * Created by LoloDev on 4/5/2017.
 * <p>
 * Permissions build center
 */

@SuppressWarnings("WeakerAccess")
public class PermissionWrapper {

    private Context context;
    private String rationalMessage;
    private String[] permissions;
    private boolean permissionGoSettings;
    private String permissionGoSettingsMessage;
    private OnRequestPermissionsCallBack onRequestPermissionsCallBack;
    private CallBackBroadcastReceiver callBackBroadcastReceiver = new CallBackBroadcastReceiver();

    public static class Builder {

        public PermissionWrapper permissionWrapper = new PermissionWrapper();

        public Builder(Context context) {
            permissionWrapper.context = context;
        }

        public Builder addPermissionRationale(String rationalMessage) {
            permissionWrapper.rationalMessage = rationalMessage;
            return this;
        }

        public Builder addPermissions(String[] permissions) {
            permissionWrapper.permissions = permissions;
            return this;
        }

        public Builder addPermissionsGoSettings(boolean permissionGoSettings, String permissionGoSettingsMessage) {
            permissionWrapper.permissionGoSettings = permissionGoSettings;
            permissionWrapper.permissionGoSettingsMessage = permissionGoSettingsMessage;
            return this;
        }

        public Builder addPermissionsGoSettings(boolean permissionGoSettings) {
            permissionWrapper.permissionGoSettings = permissionGoSettings;
            permissionWrapper.permissionGoSettingsMessage = null;
            return this;
        }

        public Builder addRequestPermissionsCallBack(OnRequestPermissionsCallBack callBack) {
            permissionWrapper.onRequestPermissionsCallBack = callBack;
            return this;
        }

        public PermissionWrapper build() {
            return permissionWrapper;
        }
    }

    public void request() {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        Intent intent = RequestPermissionsActv.newIntent(context, permissions, rationalMessage, permissionGoSettings, permissionGoSettingsMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        LocalBroadcastManager.getInstance(context).registerReceiver(callBackBroadcastReceiver, new IntentFilter(context.getPackageName()));
    }

    class CallBackBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (onRequestPermissionsCallBack == null) {
                return;
            }
            boolean result = intent.getBooleanExtra(Constants.GRANT, false);
            if (result) {
                onRequestPermissionsCallBack.onGrant();
            } else {
                String permission = intent.getStringExtra(Constants.DENIED);
                onRequestPermissionsCallBack.onDenied(permission);
            }
            LocalBroadcastManager.getInstance(PermissionWrapper.this.context).unregisterReceiver(callBackBroadcastReceiver);
        }
    }
}
