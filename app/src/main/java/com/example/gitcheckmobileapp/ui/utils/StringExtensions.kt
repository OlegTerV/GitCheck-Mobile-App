package com.example.gitcheckmobileapp.ui.utils

import java.text.SimpleDateFormat
import java.time.ZoneOffset.UTC
import java.util.Locale
import java.util.TimeZone

fun String.toReadbleDate(): String {
    try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.US)
        val date = parser.parse(this)
        val formatter = SimpleDateFormat("MMM d, yyyy", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone(UTC)
        return formatter.format(date)
    } catch (e: Exception) {
        return this
    }
}