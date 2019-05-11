package brainheap.common.tools

import java.util.*

fun getCurrentUTCTime() : Date {
    return Calendar.getInstance(TimeZone.getTimeZone("UTC")).time
}

fun generateUUID() : String {
    return UUID.randomUUID().toString()
}
