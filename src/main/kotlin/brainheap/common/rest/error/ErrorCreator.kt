package brainheap.common.rest.error

import brainheap.common.rest.error.model.ErrorInfo
import com.mongodb.MongoWriteException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ErrorCreator {

    fun errorFromMongoDb(exception: MongoWriteException): ErrorInfo {
        val error = MongoErrorCode.valueOf(exception.code)
        return ErrorInfo(exception.localizedMessage,error.status)
    }
}

enum class MongoErrorCode(val errorCode: Int, val status: HttpStatus) {
    BAD_VALUE(2, HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR(8, HttpStatus.INTERNAL_SERVER_ERROR),
    NAMESPACE_NOT_FOUND(26, HttpStatus.INTERNAL_SERVER_ERROR),
    EXCEEDED_TIME_LIMIT(50, HttpStatus.INTERNAL_SERVER_ERROR),
    COMMAND_NOT_FOUND(59, HttpStatus.INTERNAL_SERVER_ERROR),
    WRITE_CONCERN_ERROR(64, HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_MASTER(10107, HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_KEY(11000, HttpStatus.CONFLICT),
    DUPLICATE_KEY_UPDATE(11001, HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_KEY_CAPPED(12582, HttpStatus.INTERNAL_SERVER_ERROR),
    UNRECOGNIZED_COMMAND(13390, HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_MASTER_NO_SLAVE_OK(13435, HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_MASTER_OR_SECONDARY(13436, HttpStatus.INTERNAL_SERVER_ERROR),
    CANT_OPEN_DB_IN_READ_LOCK(15927, HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN(-1, HttpStatus.INTERNAL_SERVER_ERROR);

    companion object {
        fun valueOf(code: Int): MongoErrorCode = values().find { item -> item.errorCode == code } ?: UNKNOWN
    }


}