package com.fangda.erp.rmms.stfg.web.advice

import com.fangda.erp.libs.dto.InvokeResultDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange

/**
 * @author yuhb
 * @date 2020/12/25
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    fun globalException(exchange: ServerWebExchange, exception: Exception): InvokeResultDto {
        return InvokeResultDto.failResult(exception.message ?: "")
    }
}