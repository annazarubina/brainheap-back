package brainheap.common.rest.handler

import brainheap.common.exceptions.DataAlreadyExistException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class DataAlreadyExistExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataAlreadyExistException::class)
    fun handleConflict(exception: DataAlreadyExistException) {
    }
}
