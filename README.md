# PermissionsWrapper
PermissionsWrapper is the easiest way to manage Android Marshmallow and Nougat runtime permissions.

## Installation

Add dependencies
```Gradle
dependencies {
    compile 'com.github.lolucosmin:PermissionsWrapper:PermissionsWrapper_1.0'
}
```

Add repositories
```Gradle
repositories {
        maven { url "https://jitpack.io" }
}
```

## How To Use
Single permission request
```Gradle
new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})
                //enable rationale message with a custom message
                .addPermissionRationale("Rationale message")
                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
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
``` 

Multiple permissions request
```Gradle
new PermissionWrapper.Builder(this)
                .addPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
                //enable rationale message with a custom message
                .addPermissionRationale("Rationale message")
                //show settings dialog,in this case with default message base on requested permission/s
                .addPermissionsGoSettings(true)
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
``` 
