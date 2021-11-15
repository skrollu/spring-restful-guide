package com.example.springrestguide.controller.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.springrestguide.controller.EmployeeController;
import com.example.springrestguide.jpa.Employee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * Simply put, you need to define a function that converts Employee objects to
 * EntityModel<Employee> objects. While you could easily code this method
 * yourself, there are benefits down the road of implementing Spring HATEOAS’s
 * RepresentationModelAssembler interface—which will do the work for you.
 * 
 * @author Mathieu
 *
 */
@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

	@Override
	public EntityModel<Employee> toModel(Employee employee) {
		return EntityModel.of(employee, //
				linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
				linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
	}
}
