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
package lolodev.permissionswrapper;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import lolodev.permissionswrapper.constants.Constants;
import lolodev.permissionswrapper.utils.PermissionUtils;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

/**
 * Created by LoloDev on 4/5/2017.
 * <p>
 * Permissions own activity base on dialog style
 */
@SuppressWarnings("SpellCheckingInspection")
public class RequestPermissionsScreen extends AppCompatActivity {

    private static final String PERMISSIONS = "permissions";
    private static final String PERMISSION_RATIONAL_MESSAGE = "rationalMessage";
    private static final String PERMISSIONS_GO_SETTINGS = "permissionGoSettings";
    private static final String PERMISSIONS_GO_SETTINGS_MESSAGE = "permissionGoSettingsMessage";
    private static final int REQUEST_CODE = 1000;
    private String rationalMessage;
    private String[] permissions;
    private boolean permissionGoSettings;
    private String permissionGoSettingsMessage;

    public static Intent newIntent(@NonNull AppCompatActivity appCompatActivity, String[] permissions, String explain, boolean permissionGoSettings, String permissionGoSettingsMessage) {
        Intent intent = null;
        try {
            intent = new Intent(appCompatActivity, RequestPermissionsScreen.class);
            Bundle extras = new Bundle();
            extras.putStringArray(PERMISSIONS, permissions);
            extras.putString(PERMISSION_RATIONAL_MESSAGE, explain);
            extras.putBoolean(PERMISSIONS_GO_SETTINGS, permissionGoSettings);
            extras.putString(PERMISSIONS_GO_SETTINGS_MESSAGE, permissionGoSettingsMessage);
            intent.putExtras(extras);
        } catch (Exception ex) {
            Log.e(PermissionWrapper.class.getName(), String.valueOf(ex.getMessage()));
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addInitData();
        addCheckPermission();
    }

    private void addInitData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.rationalMessage = extras.getString(PERMISSION_RATIONAL_MESSAGE);
            this.permissions = extras.getStringArray(PERMISSIONS);
            this.permissionGoSettings = extras.getBoolean(PERMISSIONS_GO_SETTINGS);
            this.permissionGoSettingsMessage = extras.getString(PERMISSIONS_GO_SETTINGS_MESSAGE);
        }
    }

    private void addCheckPermission() {
        int deniedIndex = checkSelfPermissions(this.permissions);
        if (deniedIndex != -1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, this.permissions[deniedIndex])) {
                if (TextUtils.isEmpty(this.rationalMessage)) {
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
        ActivityCompat.requestPermissions(this, this.permissions, REQUEST_CODE);
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
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
        args.putString(Constants.DENIED, this.permissions[index]);

        Intent intent = new Intent();
        intent.putExtras(args);
        intent.setAction(getPackageName());

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        if (this.permissionGoSettings) {
            showPermissionsSettingsDialog(index);
        } else {
            finish();
        }
    }


    @SuppressLint("RestrictedApi")
    private void showRationalDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.RationalDialogStyle))
                .setMessage(this.rationalMessage)
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

    @SuppressLint("RestrictedApi")
    private void showPermissionsSettingsDialog(int index) {
        String message;
        if (!TextUtils.isEmpty(this.permissionGoSettingsMessage)) {
            message = this.permissionGoSettingsMessage;
        } else {
            message = PermissionUtils.getGoSettingsMessage(this, this.permissions[index]);
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