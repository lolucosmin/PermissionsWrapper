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
package lolodev.permissionslib;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;

import lolodev.permissionslib.constants.Constants;
import lolodev.permissionslib.utils.PermissionUtils;

/**
 * Created by LoloDev on 4/5/2017.
 * <p>
 * Permissions own activity base on dialog style
 */
@SuppressWarnings("SpellCheckingInspection")
public class RequestPermissionsActv extends AppCompatActivity {

    private static final String PERMISSIONS = "permissions";
    private static final String PERMISSION_RATIONAL_MESSAGE = "rationalMessage";
    private static final String PERMISSIONS_GO_SETTINGS = "permissionGoSettings";
    private static final String PERMISSIONS_GO_SETTINGS_MESSAGE = "permissionGoSettingsMessage";
    private static final int REQUEST_CODE = 1000;
    private String rationalMessage;
    private String[] permissions;
    private boolean permissionGoSettings;
    private String permissionGoSettingsMessage;

    public static Intent newIntent(Context context, String[] permissions, String explain, boolean permissionGoSettings, String permissionGoSettingsMessage) {
        Intent intent = new Intent(context, RequestPermissionsActv.class);
        Bundle extras = new Bundle();
        extras.putStringArray(PERMISSIONS, permissions);
        extras.putString(PERMISSION_RATIONAL_MESSAGE, explain);
        extras.putBoolean(PERMISSIONS_GO_SETTINGS, permissionGoSettings);
        extras.putString(PERMISSIONS_GO_SETTINGS_MESSAGE, permissionGoSettingsMessage);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        checkPermission();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        this.rationalMessage = extras.getString(PERMISSION_RATIONAL_MESSAGE);
        this.permissions = extras.getStringArray(PERMISSIONS);
        this.permissionGoSettings = extras.getBoolean(PERMISSIONS_GO_SETTINGS);
        this.permissionGoSettingsMessage = extras.getString(PERMISSIONS_GO_SETTINGS_MESSAGE);
    }

    private void checkPermission() {
        int deniedIndex = checkSelfPermissions(permissions);
        if (deniedIndex != -1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[deniedIndex])) {
                if (TextUtils.isEmpty(rationalMessage)) {
                    requestPermission();
                } else {
                    showRationalDialog();
                }

            } else {
                requestPermission();
            }
        } else {
            sendGrantMessage();
        }
    }

    private int checkSelfPermissions(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }
        return -1;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                int index = PermissionUtils.verifyPermissions(grantResults);
                if (index == -1) {
                    sendGrantMessage();
                } else {
                    sendDeniedMessage(index);
                }
                break;
        }
    }

    private void sendGrantMessage() {
        Bundle args = new Bundle();
        args.putBoolean(Constants.GRANT, true);

        Intent intent = new Intent();
        intent.putExtras(args);
        intent.setAction(getPackageName());

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    private void sendDeniedMessage(int index) {
        Bundle args = new Bundle();
        args.putString(Constants.DENIED, permissions[index]);

        Intent intent = new Intent();
        intent.putExtras(args);
        intent.setAction(getPackageName());

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (permissionGoSettings) {
            showPermissionsSettingsDialog(index);
        } else {
            finish();
        }
    }


    private void showRationalDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.RationalDialogStyle))
                .setMessage(rationalMessage)
                .setPositiveButton(getString(R.string.enabled), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show()
                .setCancelable(false);
    }

    private void showPermissionsSettingsDialog(int index) {
        String message;
        if (!TextUtils.isEmpty(permissionGoSettingsMessage)) {
            message = permissionGoSettingsMessage;
        } else {
            message = PermissionUtils.getGoSettingsMessage(this, permissions[index]);
        }
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.RationalDialogStyle))
                .setTitle(getString(R.string.permission_go_settings_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show()
                .setCancelable(false);
    }
}
