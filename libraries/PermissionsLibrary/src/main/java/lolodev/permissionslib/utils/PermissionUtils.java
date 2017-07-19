package lolodev.permissionslib.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import lolodev.permissionslib.R;

/**
 * Created by Cosmin Lolu on 4/5/2017.
 * <p>
 * Permissions util part
 */

public class PermissionUtils {

    public static int verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return -1;
        }

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }
        return -1;
    }

    public static String getGoSettingsMessage(Context context, String permission) {
        String message;

        switch (permission) {
            case Manifest.permission.CAMERA:
                message = context.getString(R.string.permission_android_permission_CAMERA);
                break;

            case Manifest.permission.READ_EXTERNAL_STORAGE:
                message = context.getString(R.string.permission_android_permission_READ_EXTERNAL_STORAGE);
                break;

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                message = context.getString(R.string.permission_android_permission_WRITE_EXTERNAL_STORAGE);
                break;

            case Manifest.permission.READ_CONTACTS:
                message = context.getString(R.string.permission_android_permission_READ_CONTACTS);
                break;

            case Manifest.permission.WRITE_CONTACTS:
                message = context.getString(R.string.permission_android_permission_WRITE_CONTACTS);
                break;

            case Manifest.permission.READ_CALENDAR:
                message = context.getString(R.string.permission_android_permission_READ_CALENDAR);
                break;

            case Manifest.permission.WRITE_CALENDAR:
                message = context.getString(R.string.permission_android_permission_WRITE_CALENDAR);
                break;

            case Manifest.permission.ACCESS_COARSE_LOCATION:
                message = context.getString(R.string.permission_android_permission_WRITE_CALENDAR);
                break;

            case Manifest.permission.ACCESS_FINE_LOCATION:
                message = context.getString(R.string.permission_android_permission_WRITE_CALENDAR);
                break;

            default:
                message = context.getString(R.string.permission_default_message);
                break;
        }

        return message;
    }

}
