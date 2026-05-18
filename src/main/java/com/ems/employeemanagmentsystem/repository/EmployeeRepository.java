package com.ems.employeemanagmentsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.employeemanagmentsystem.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
