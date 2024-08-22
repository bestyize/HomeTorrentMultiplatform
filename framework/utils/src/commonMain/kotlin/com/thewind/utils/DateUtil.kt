package com.thewind.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern


/**
 * @author: read
 * @date: 2023/8/20 下午2:38
 * @description:
 */

@OptIn(FormatStringsInDatetimeFormats::class)
private val sdf by lazy {
    LocalDateTime.Format { byUnicodePattern("yyyy-MM-dd") }
}

fun Long?.toDate(): String {
    this ?: return ""
    return "2008"
}