package com.eduprime.eduprimeserver.exceptions;

import com.eduprime.eduprimeserver.common.HttpStatusCodes;
import com.eduprime.eduprimeserver.common.HttpStatusMessages;
import com.eduprime.eduprimeserver.dtos.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage("Bad Request");
        response.setResult(ex.getMessage());

        log.warn("{}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpStatusCodes.NOT_FOUND);
        response.setMessage(HttpStatusMessages.NOT_FOUND);
        response.setResult("Not found: " + ex.getMessage());

        log.warn("{}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<String>> handleBadRequestException(BadRequestException ex) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpStatusCodes.BAD_REQUEST);
        response.setMessage(HttpStatusMessages.BAD_REQUEST);
        response.setResult(ex.getMessage());

        log.warn("{}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatusCodes.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<BaseResponse<String>> handleForbiddenException(ForbiddenException ex) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpStatusCodes.FORBIDDEN);
        response.setMessage(HttpStatusMessages.FORBIDDEN);
        response.setResult(ex.getMessage());

        log.warn("{}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<BaseResponse<String>> handleServiceUnavailableException(ServiceUnavailableException ex) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpStatusCodes.SERVICE_UNAVAILABLE);
        response.setMessage(HttpStatusMessages.SERVICE_UNAVAILABLE);
        response.setResult(ex.getMessage());

        log.error("{}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<BaseResponse<String>> nnauthorizedException(UnauthorizedException ex) {
        BaseResponse<String> response = new BaseResponse<>();
        response.setCode(HttpStatusCodes.UNAUTHORIZED);
        response.setMessage(HttpStatusMessages.UNAUTHORIZED);
        response.setResult(ex.getMessage());

        log.error("{}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
