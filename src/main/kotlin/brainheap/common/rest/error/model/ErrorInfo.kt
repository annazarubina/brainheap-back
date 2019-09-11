package brainheap.common.rest.error.model

import org.springframework.http.HttpStatus

data class ErrorInfo(
        val description:String,
        @Transient
        val code: HttpStatus
)