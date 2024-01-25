package org.team.b6.catchtable.global.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.team.b6.catchtable.global.dto.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler(
    private val httpServletRequest: HttpServletRequest
) {
    @ExceptionHandler(InvalidStoreSearchingValuesException::class)
    fun handleInvalidStoreSearchingValuesException(e: InvalidStoreSearchingValuesException) =
        ResponseEntity.badRequest().body(
            ErrorResponse(
                httpStatus = "401 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                httpStatus = "404 Not Found",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    @ExceptionHandler(IllegalStateException::class)
    fun handleAlreadyAppliedException(e: IllegalStateException) =
        ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse(
                httpStatus = "409 Conflict",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "400 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    @ExceptionHandler(DuplicatedValueException::class)
    fun handleDuplicatedValueException(e: DuplicatedValueException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                httpStatus = "401 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )

    @ExceptionHandler(InvalidCredentialException::class)
    fun handleInvalidCredentialException(e: InvalidCredentialException) =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse(
                httpStatus = "401 Unauthorized",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )
}