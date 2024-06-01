package com.caelum_software.wowo.utils

import android.app.Activity
import android.content.Intent
import android.widget.Toast


fun Activity.showToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

fun Activity.shareAppWithFriends() {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    val body = "WoWo"
    val sub = "https://play.google.com/store/apps/details?id=com.caelum_software.wowo"
    intent.putExtra(Intent.EXTRA_TEXT, body)
    intent.putExtra(Intent.EXTRA_TEXT, sub)
    startActivity(Intent.createChooser(intent, "Share with friends"))
}