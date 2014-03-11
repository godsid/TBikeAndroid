package info.srihawong.tbike;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


/**
 * Created by Banpot.S on 11/2/2557.
 */
public class Util {
    public static String TAG = "tui";
    public static final String PROPERTY_APP_VERSION = "appVersion";

    String regid;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID;

    public static void Log(String msg){
        Log.d(TAG,msg);
    }
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }

    }



}


