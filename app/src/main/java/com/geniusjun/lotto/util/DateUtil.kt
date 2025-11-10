package com.geniusjun.lotto.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun todayString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(Date())
}
