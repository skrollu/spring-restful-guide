package com.example.springrestguide.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.springrestguide.controller.assembler.EmployeeModelAssembler;
import com.example.springrestguide.dao.EmployeeRepository;
import com.example.springrestguide.exceptions.EmployeeNotFoundException;
import com.example.springrestguide.jpa.Employee;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	private final EmployeeRepository repository;

	private final EmployeeModelAssembler assembler;

	public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	/**
	 * Using HATEOAS it returns a linked (hypermedia) response which is more
	 * restful:
	 * 
	 * 
	 * The return type of the method has changed from Employee to
	 * EntityModel<Employee>. EntityModel<T> is a generic container from Spring
	 * HATEOAS that includes not only the data but a collection of links.
	 * 
	 * linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel() asks that
	 * Spring HATEOAS build a link to the EmployeeController 's one() method, and
	 * flag it as a self link.
	 * 
	 * linkTo(methodOn(EmployeeController.class).all()).withRel("employees") asks
	 * Spring HATEOAS to build a link to the aggregate root, all(), and call it
	 * "employees".
	 * 
	 * 
	 * @return
	 */
	@GetMapping("/employees")
	public CollectionModel<EntityModel<Employee>> all() {
		List<EntityModel<Employee>> employees = repository.findAll().stream() //
				.map(assembler::toModel) //
				.collect(Collectors.toList());

		return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
	}

	/*
	 * @GetMapping("/employees") CollectionModel<EntityModel<Employee>> all() {
	 * 
	 * List<EntityModel<Employee>> employees = repository.findAll().stream()
	 * .map(employee -> EntityModel.of(employee,
	 * linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(
	 * ), linkTo(methodOn(EmployeeController.class).all()).withRel("employees")))
	 * .collect(Collectors.toList());
	 * 
	 * return CollectionModel.of(employees,
	 * linkTo(methodOn(EmployeeController.class).all()).withSelfRel()); }
	 */
	/*
	 * // Aggregate root // tag::get-aggregate-root[]
	 * 
	 * @GetMapping("/employees") List<Employee> all() { return repository.findAll();
	 * } // end::get-aggregate-root[]
	 */

	/**
	 * POST that handles "old" and "new" client requests
	 * 
	 * 
	 * The new Employee object is saved as before. But the resulting object is
	 * wrapped using the EmployeeModelAssembler.
	 * 
	 * Spring MVC’s ResponseEntity is used to create an HTTP 201 Created status
	 * message. This type of response typically includes a Location response header,
	 * and we use the URI derived from the model’s self-related link.
	 * 
	 * Additionally, return the model-based version of the saved object.
	 * 
	 * 
	 * @param newEmployee
	 * @return
	 */
	@PostMapping("/employees")
	public ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
		EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	/*
	 * @PostMapping("/employees") Employee newEmployee(@RequestBody Employee
	 * newEmployee) { return repository.save(newEmployee); }
	 */

	@GetMapping("/employees/{id}")
	public
	EntityModel<Employee> one(@PathVariable Long id) {
		Employee employee = repository.findById(id) //
				.orElseThrow(() -> new EmployeeNotFoundException(id));

		return assembler.toModel(employee);
	}

	/*
	 * @GetMapping("/employees/{id}") EntityModel<Employee> one(@PathVariable Long
	 * id) {
	 * 
	 * Employee employee = repository.findById(id) // .orElseThrow(() -> new
	 * EmployeeNotFoundException(id));
	 * 
	 * return EntityModel.of(employee, //
	 * linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
	 * linkTo(methodOn(EmployeeController.class).all()).withRel("employees")); }
	 */

	// Single item
	/*
	 * @GetMapping("/employees/{id}") Employee one(@PathVariable Long id) {
	 * 
	 * return repository.findById(id) .orElseThrow(() -> new
	 * EmployeeNotFoundException(id)); }
	 */

	@PutMapping("/employees/{id}")
	public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
		Employee updatedEmployee = repository.findById(id) //
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				}) //
				.orElseGet(() -> {
					newEmployee.setId(id);
					return repository.save(newEmployee);
				});

		EntityModel<Employee> entityModel = assembler.toModel(updatedEmployee);

		return ResponseEntity //
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
				.body(entityModel);
	}

	/*
	 * @PutMapping("/employees/{id}") Employee replaceEmployee(@RequestBody Employee
	 * newEmployee, @PathVariable Long id) {
	 * 
	 * return repository.findById(id) .map(employee -> {
	 * employee.setName(newEmployee.getName());
	 * employee.setRole(newEmployee.getRole()); return repository.save(employee); })
	 * .orElseGet(() -> { newEmployee.setId(id); return
	 * repository.save(newEmployee); }); }
	 */

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	/*
	 * @DeleteMapping("/employees/{id}") void deleteEmployee(@PathVariable Long id)
	 * { repository.deleteById(id); }
	 */
	public EmployeeModelAssembler getAssembler() {
		return assembler;
	}
}
