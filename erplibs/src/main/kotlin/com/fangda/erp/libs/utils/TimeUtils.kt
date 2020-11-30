/*
 * Copyright (c) 2020.
 * 方大特钢科技股份有限公司
 * all rights reserved
 */

package com.fangda.erp.libs.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author yuhb
 * @date 2020/5/20
 */
object TimeUtils {
    fun now(): LocalDateTime {
        return LocalDateTime.now(ZoneId.systemDefault())
    }

    fun format(dateTime: LocalDateTime, pattern: String = "yyyyMMdd"): String {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.CHINA))
    }

    fun format(dateTime: Date, pattern: String = "yyyyMMdd"): String {
        return SimpleDateFormat(pattern).format(dateTime)
    }

    fun parse(timeStr: String, pattern: String = "yyyy-MM-dd"): LocalDateTime {
        return LocalDateTime.from(DateTimeFormatter.ofPattern(pattern, Locale.CHINA).parse(timeStr))
    }
}