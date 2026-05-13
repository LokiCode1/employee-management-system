package com.ems.employeemanagmentsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.employeemanagmentsystem.entity.Employee;
import com.ems.employeemanagmentsystem.exception.EmployeeNotFoundException;
import com.ems.employeemanagmentsystem.repository.EmployeeRepository;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Employee findById(Integer id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	@Transactional
	public Employee create(Employee employee) {

		return employeeRepository.save(employee);
	}

	@Transactional
	public Employee update(Integer id, Employee incoming) {
		if (!employeeRepository.existsById(id)) {
			throw new EmployeeNotFoundException(id);
		}
		incoming.setEmpId(id);
		return employeeRepository.save(incoming);
	}

	@Transactional
	public void deleteById(Integer id) {
		if (!employeeRepository.existsById(id)) {
			throw new EmployeeNotFoundException(id);
		}
		employeeRepository.deleteById(id);
	}
}
