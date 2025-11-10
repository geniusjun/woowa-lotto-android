package com.geniusjun.lotto.util

import android.content.Context

private const val PREF_NAME = "fortune_prefs"
private const val KEY_LAST_DATE = "last_fortune_date"

fun saveLastFortuneDate(context: Context, date: String) {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    prefs.edit().putString(KEY_LAST_DATE, date).apply()
}

fun loadLastFortuneDate(context: Context): String? {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_LAST_DATE, null)
}
