# Employee Management System - Project Structure

This document explains file segregation for quick developer onboarding.

## Backend source (`src/main/java/com/ems/employeemanagmentsystem`)

- `EmployeemanagmentsystemApplication.java`  
  Spring Boot entry point.

- `controller/EmployeeController.java`  
  Mixed controller:
  - MVC endpoints for Thymeleaf pages (`/employees/dashboard`, `/employees/add`, `/employees/save`)
  - JSON API endpoints (`/employees/api/*`)

- `service/EmployeeService.java`  
  Business logic and transactional operations.

- `repository/EmployeeRepository.java`  
  Data-access layer (Spring Data JPA).

- `entity/Employee.java`  
  JPA entity + validation + JSON field mapping (`@JsonProperty("empid")`).

- `exception/`  
  Centralized exception handling and API error response models.

- `config/OpenApiConfig.java`  
  OpenAPI/Swagger metadata configuration.

## Frontend templates (`src/main/resources/templates`)

- `layout.html`  
  Base shell (sidebar, topbar, search, Tailwind/Lucide setup).

- `dashboard.html`  
  Main page with stats and employee table.

- `employee-form.html`  
  Add/Edit form with Thymeleaf validation messages.

## Config (`src/main/resources`)

- `application.properties`  
  App, database, and H2 console configuration.

## Swagger / OpenAPI

- UI URL: `/swagger-ui/index.html`
- OpenAPI JSON: `/v3/api-docs`
- Documented scope: JSON endpoints under `/employees/api/*`
