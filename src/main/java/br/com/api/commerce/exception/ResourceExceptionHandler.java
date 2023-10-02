package br.com.api.commerce.exception;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResourceExceptionHandler {
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NotFoundException.class})
    public CampoInvalido handlerNotFoundException(NotFoundException exception) {
        CampoInvalido campoInvalido = new CampoInvalido(exception.getCampo(), exception.getMensagem());
        return campoInvalido;
    }

}
