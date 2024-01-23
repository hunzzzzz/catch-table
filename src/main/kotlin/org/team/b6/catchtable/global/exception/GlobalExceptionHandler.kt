package org.team.b6.catchtable.global.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.team.b6.catchtable.global.dto.ErrorResponse

@RestControllerAdvice
class GlobalExceptionHandler(
    private val httpServletRequest: HttpServletRequest
) {
    @ExceptionHandler(InvalidStoreSearchingValuesException::class)
    fun handleInvalidStoreCategoryException(e: InvalidStoreSearchingValuesException) =
        ResponseEntity.badRequest().body(
            ErrorResponse(
                httpStatus = "401 Bad Request",
                message = e.message.toString(),
                path = httpServletRequest.requestURI
            )
        )
}