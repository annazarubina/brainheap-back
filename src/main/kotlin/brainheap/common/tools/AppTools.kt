package brainheap.common.tools

import org.springframework.util.StringUtils
import java.util.*

fun getCurrentUTCTime(): Date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time

fun generateUUID() = UUID.randomUUID().toString()

fun removeQuotes(string: String?) = string?.removeSurrounding("\"")?.removeSurrounding("'")

fun removeQuotesAndTrimWhitespaces(string: String?) = string?.let{ removeQuotes(StringUtils.trimTrailingWhitespace(StringUtils.trimLeadingWhitespace(it))) }
