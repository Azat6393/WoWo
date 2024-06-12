package utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        const val DATABASE_NAME = "otborta_preferences"

        const val USER_ID = "user_id"
        const val REVIEW = "review"
    }

    var userId: String?
        get() = preferences.getString(USER_ID, null)
        set(value) {
            preferences.edit { putString(USER_ID, value) }
        }

    var review: Int
        get() = preferences.getInt(REVIEW, 0)
        set(value) {
            preferences.edit { putInt(REVIEW, value) }
        }
}

fun getSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences(SharedPreferencesHelper.DATABASE_NAME, Context.MODE_PRIVATE)