package lolodev.permissionslib.callback;

/**
 * Created by Cosmin Lolu on 4/5/2017.
 *
 * Permissions callback witch return result to GUI
 */
public interface OnRequestPermissionsCallBack {

    void onGrant();

    void onDenied(String permission);
}
