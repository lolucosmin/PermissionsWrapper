package lolodev.permissionslib.wrapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import lolodev.permissionslib.constants.Constants;
import lolodev.permissionslib.RequestPermissionsActv;
import lolodev.permissionslib.callback.OnRequestPermissionsCallBack;

/**
 * Created by Cosmin Lolu on 4/5/2017.
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
