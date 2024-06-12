package utils

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

actual class UniqueIdGenerator(
    private val context: Context,
) {
    @SuppressLint("HardwareIds")
    actual fun getId(): String {
        val sharedPref = SharedPreferencesHelper(getSharedPreferences(context))
        if (!sharedPref.userId.isNullOrEmpty()) {
            return sharedPref.userId!!
        }
        val deviceId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        sharedPref.userId = deviceId
        return deviceId ?: "guest_user"
    }
}