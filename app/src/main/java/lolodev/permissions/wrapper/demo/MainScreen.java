package lolodev.permissions.wrapper.demo;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import lolodev.permissionswrapper.callback.OnRequestPermissionsCallBack;
import lolodev.permissionswrapper.wrapper.PermissionWrapper;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testButton = (Button) findViewById(R.id.test_button);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAskPermissionSample1();
            }
        });
    }

    private void onAskPermissionSample1() {
        new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION})
                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(false)
                //enable callback to know what option was choose
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was grant.");
                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was not grant.");
                    }
                }).build().request();
    }

    private void onAskPermissionSample2() {
        new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
                //enable rationale message with a custom message
                .addPermissionRationale("Rationale message")
                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true, "Custom message for settings dialog")
                //enable callback to know what option was choose
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was grant.");
                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was not grant.");
                    }
                }).build().request();
    }
}
