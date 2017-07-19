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
 * Created by Cosmin Lolu on 4/5/2017.
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
