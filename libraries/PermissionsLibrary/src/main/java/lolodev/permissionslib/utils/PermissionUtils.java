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
package lolodev.permissionslib.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import lolodev.permissionslib.R;

/**
 * Created by LoloDev on 4/5/2017.
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
