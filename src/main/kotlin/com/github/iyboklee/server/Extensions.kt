package com.github.iyboklee.server

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun org.joda.time.DateTime.toJavaLocalDate() = java.time.LocalDate.of(year, monthOfYear, dayOfMonth)!!

val yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd")
val yyyyMMdd2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun java.time.LocalDate.yyyyMMdd(): String = format(yyyyMMdd2)

fun String.toLocalDate(): LocalDate? = when(length) {
    8 -> LocalDate.parse(this, yyyyMMdd)
    10 -> LocalDate.parse(this, yyyyMMdd2)
    else -> null
}