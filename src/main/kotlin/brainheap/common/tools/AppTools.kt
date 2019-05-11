package brainheap.common.tools

import java.util.*

fun getCurrentUTCTime(): Date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time

fun generateUUID() = UUID.randomUUID().toString()

fun removeQuotes(string: String?) = string?.removeSurrounding("\"")?.removeSurrounding("'")

