package brainheap.common.rest.error.handler

import brainheap.common.rest.error.ErrorCreator
import brainheap.common.rest.error.model.ErrorInfo
import com.mongodb.MongoWriteException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class MongoDBWriteExceptionHandler(private val errorCreator: ErrorCreator) {
    @ExceptionHandler(MongoWriteException::class)
    fun handleDBWriteError(exception: MongoWriteException): ResponseEntity<ErrorInfo> {
        val error = errorCreator.errorFromMongoDb(exception)
        return ResponseEntity
                .status(error.code)
                .body(error)
    }
}
