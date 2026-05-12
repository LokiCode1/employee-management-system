package com.ems.employeemanagmentsystem.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmployeeNotFoundException.class)
	public String handleNotFound(EmployeeNotFoundException ex, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		return "redirect:/employees/dashboard";
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		var violations = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> new FieldViolation(err.getField(), err.getDefaultMessage()))
				.collect(Collectors.toList());
		ApiErrorResponse body = new ApiErrorResponse(
				Instant.now(),
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				"Validation failed",
				violations);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}
}
