package me.echeung.moemoekyun.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

public class DozeUtil {

    public static boolean isWhitelisted(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final String packageName = context.getPackageName();
            final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            return pm == null || pm.isIgnoringBatteryOptimizations(packageName);
        }

        return true;
    }

    public static void requestWhitelist(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final String packageName = context.getPackageName();
            final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                final Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }
        }
    }

}
