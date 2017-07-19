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
```
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
```
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


## License
```
PermissionsWrapper
Copyright (c) 2017 Lolu Cosmin (https://github.com/lolucosmin).
  
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
