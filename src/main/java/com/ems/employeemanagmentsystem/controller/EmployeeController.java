package com.ems.employeemanagmentsystem.controller;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ems.employeemanagmentsystem.entity.Employee;
import com.ems.employeemanagmentsystem.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/employees")
@Tag(name = "Employee API", description = "JSON endpoints for employee management")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping({"", "/dashboard"})
	public String dashboard(@RequestParam(name = "q", required = false) String query, Model model) {
		List<Employee> employees = employeeService.findAll();
		if (query != null && !query.isBlank()) {
			String keyword = query.toLowerCase(Locale.ROOT);
			employees = employees.stream()
					.filter(employee -> contains(employee.getName(), keyword)
							|| contains(employee.getEmail(), keyword)
							|| contains(employee.getCity(), keyword))
					.collect(Collectors.toList());
		}
		model.addAttribute("employees", employees);
		model.addAttribute("searchQuery", query == null ? "" : query);
		model.addAttribute("totalEmployees", employeeService.findAll().size());
		model.addAttribute("recentHires", Math.min(employeeService.findAll().size(), 5));
		model.addAttribute("activeCompanies", employeeService.findAll().stream()
				.map(Employee::getCompanyId)
				.filter(companyId -> companyId != null)
				.distinct()
				.count());
		return "dashboard";
	}

	@GetMapping("/new")
	public String newEmployeeForm(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute("formTitle", "Add Employee");
		return "employee-form";
	}

	@GetMapping("/add")
	public String showCreateForm(Model model) {
		model.addAttribute("employee", new Employee());
		model.addAttribute("formTitle", "Add Employee");
		return "employee-form";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model) {
		model.addAttribute("employee", employeeService.findById(id));
		model.addAttribute("formTitle", "Edit Employee");
		return "employee-form";
	}

	@PostMapping("/save")
	public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("formTitle", employee.getEmpId() == null ? "Add Employee" : "Edit Employee");
			return "employee-form";
		}
		employeeService.create(employee);
		redirectAttributes.addFlashAttribute("successMessage", "Employee saved successfully.");
		return "redirect:/employees/dashboard";
	}

	@GetMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		employeeService.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");
		return "redirect:/employees/dashboard";
	}

	@GetMapping("/")
	public String homeRedirect() {
		return "redirect:/employees/dashboard";
	}

	@GetMapping("/api/all")
	@ResponseBody
	@Operation(summary = "Get all employees", description = "Returns all employees as JSON")
	@ApiResponse(responseCode = "200", description = "Employees fetched successfully")
	public ResponseEntity<List<Employee>> getAllEmployeesApi() {
		return ResponseEntity.ok(employeeService.findAll());
	}

	@GetMapping("/api/{id}")
	@ResponseBody
	@Operation(summary = "Get employee by ID", description = "Returns one employee by empId")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee found"),
			@ApiResponse(responseCode = "404", description = "Employee not found")
	})
	public ResponseEntity<Employee> getEmployeeByIdApi(@PathVariable Integer id) {
		return ResponseEntity.ok(employeeService.findById(id));
	}

	@PostMapping("/api/create")
	@ResponseBody
	@Operation(summary = "Create employee", description = "Creates employee record using manual empId from request body")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Employee created"),
			@ApiResponse(responseCode = "400", description = "Validation failed",
					content = @Content(schema = @Schema(implementation = Employee.class)))
	})
	public ResponseEntity<Employee> createEmployeeApi(@Valid @RequestBody Employee employee) {
		Employee saved = employeeService.create(employee);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/employees/api/{id}")
				.buildAndExpand(saved.getEmpId())
				.toUri();
		return ResponseEntity.created(location).body(saved);
	}

	@PutMapping("/api/{id}")
	@ResponseBody
	@Operation(summary = "Update employee", description = "Updates an employee by path empId")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Employee updated"),
			@ApiResponse(responseCode = "404", description = "Employee not found")
	})
	public ResponseEntity<Employee> updateEmployeeApi(@PathVariable Integer id, @Valid @RequestBody Employee employee) {
		return ResponseEntity.ok(employeeService.update(id, employee));
	}

	@DeleteMapping("/api/{id}")
	@ResponseBody
	@Operation(summary = "Delete employee", description = "Deletes employee by empId")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Employee deleted"),
			@ApiResponse(responseCode = "404", description = "Employee not found")
	})
	public ResponseEntity<Void> deleteEmployeeApi(@PathVariable Integer id) {
		employeeService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	private boolean contains(String source, String keyword) {
		return source != null && source.toLowerCase(Locale.ROOT).contains(keyword);
	}
}
