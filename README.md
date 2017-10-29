# PermissionsWrapper
PermissionsWrapper is the easiest way to manage Android Marshmallow and Nougat runtime permissions.

## Installation

Add dependencies
```Gradle
dependencies {
    compile 'com.github.lolucosmin:PermissionsWrapper:version_1.5'
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
                //enable callback to know what option was choosen
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was granted.");
                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was not granted.");
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
                //enable callback to know what option was choosed
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was granted.");
                    }

                    @Override
                    public void onDenied(String permission) {
                        Log.i(MainScreen.class.getSimpleName(), "Permission was not granted.");
                    }
                }).build().request();
``` 


## License
```
BSD 2-Clause License

Copyright (c) 2017, LoloDev
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
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
```
