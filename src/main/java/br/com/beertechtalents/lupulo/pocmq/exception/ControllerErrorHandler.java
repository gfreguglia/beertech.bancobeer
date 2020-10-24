package br.com.beertechtalents.lupulo.pocmq.exception;

import java.util.ArrayList;
import java.util.List;
import javax.validation.UnexpectedTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public String handleBusinessException(BusinessException ex){
        return ex.getMessage();
    }


    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public String handleTokenInvalidException(TokenInvalidException ex){
        return ex.getMessage();
    }

}
