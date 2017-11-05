package com.mastertechsoftware.myweatherapp

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

/**
 * Handle Android 6.0 & Above permissions
 */
class PermissionsManager {
    companion object {

        /**
         * If on Marshmellow, check if we have this permission, otherwise return true
         * @param permission
         * @return true if the user has granted this permission
         */
        fun hasPermission(context: Context, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        }

        /**
         * Show we show some sort of rationale for the permission?
         * @param activity
         * @param permission
         * @return true if user already said no 1x, false if deny always
         */
        fun shouldShowRationale(activity: Activity,
                                permission: String): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            } else false
        }


        /**
         * Bring up the system permission request dialog
         * @param activity
         * @param permissions
         * @param requestCode
         */
        fun requestPermissions(activity: Activity,
                               permissions: Array<String>, requestCode: Int) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
    }

}
