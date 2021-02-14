package ua.foxminded.yakovlev.university.exceptionhandler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import ua.foxminded.yakovlev.university.exception.NotFoundException;
import java.lang.IllegalArgumentException;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    
	@ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ExceptionInfo handleNotFound(HttpServletRequest req, NotFoundException exception) {    	
		return new ExceptionInfo(req.getRequestURI(), exception.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ExceptionInfo handleIllegalArgument(HttpServletRequest req, IllegalArgumentException exception) {    	    	
		return new ExceptionInfo(req.getRequestURI(), exception.getMessage());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ExceptionInfo handleMethodArgumentNotValid(HttpServletRequest req, MethodArgumentNotValidException exception) {
                          
            BindingResult result = exception.getBindingResult();                
            String errorsMessage = result.getAllErrors().stream()
            		.map((e) -> e.getDefaultMessage())
            		.reduce((s1, s2) -> s1 + "; " + s2).orElse("");
             
            return new ExceptionInfo(req.getRequestURI(), errorsMessage);
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ExceptionInfo MethodArgumentTypeMismatchException(HttpServletRequest req, MethodArgumentTypeMismatchException exception) {
                          
            String errorsMessage = exception.getRootCause().getMessage();
             
            return new ExceptionInfo(req.getRequestURI(), errorsMessage);
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ExceptionInfo handleAuthenticationException(HttpServletRequest req, AuthenticationException exception) {    	    	
		return new ExceptionInfo(req.getRequestURI(), exception.getMessage());
    }
}