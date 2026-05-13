package com.ems.employeemanagmentsystem.exception;

import java.time.Instant;
import java.util.List;

public class ApiErrorResponse {

	private final Instant timestamp;
	private final int status;
	private final String error;
	private final String message;
	private final List<FieldViolation> fieldErrors;

	public ApiErrorResponse(Instant timestamp, int status, String error, String message,
			List<FieldViolation> fieldErrors) {
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.fieldErrors = fieldErrors;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public List<FieldViolation> getFieldErrors() {
		return fieldErrors;
	}
}
