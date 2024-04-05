package com.nizarfadlan.aplikasigithubuser.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object Helper {
    fun Context?.handleError(tag: String, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Log.e(tag, "Error: $message")
        this?.let {
            Toast.makeText(it, "Error: $message", duration).show()
        }
    }
}