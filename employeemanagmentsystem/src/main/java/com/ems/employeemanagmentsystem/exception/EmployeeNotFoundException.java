package com.ems.employeemanagmentsystem.exception;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(Integer id) {
		super("Employee not found with EMPID: " + id);
	}
}
