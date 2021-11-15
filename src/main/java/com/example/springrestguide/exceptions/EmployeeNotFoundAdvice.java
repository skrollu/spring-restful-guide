package com.example.springrestguide.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmployeeNotFoundAdvice {

	/**
	 * @ControllerAdvice is a specialization of the @Component annotation which
	 *                   allows to handle exceptions across the whole application in
	 *                   one global handling component. It can be viewed as an
	 *                   interceptor of exceptions thrown by methods annotated
	 *                   with @RequestMapping and similar.
	 */
	@ResponseBody
	@ExceptionHandler(EmployeeNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String employeeNotFoundHandler(EmployeeNotFoundException ex) {
		return "ADVICE: " + ex.getMessage();
	}
}
