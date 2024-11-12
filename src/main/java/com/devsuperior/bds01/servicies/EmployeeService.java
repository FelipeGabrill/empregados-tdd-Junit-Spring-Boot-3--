package com.devsuperior.bds01.servicies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds01.dto.EmployeeDTO;
import com.devsuperior.bds01.entities.Department;
import com.devsuperior.bds01.entities.Employee;
import com.devsuperior.bds01.repositories.DepartmentRepository;
import com.devsuperior.bds01.repositories.EmployeeRepository;
import com.devsuperior.bds01.servicies.exceptions.ResourceNotFoundException;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;
	
	@Autowired DepartmentRepository departmentRepository;
	
	@Transactional(readOnly = true)
	public Page<EmployeeDTO> findAll(Pageable pageable) {
		Page<Employee> result = repository.findAll(pageable);
		return result.map(x -> new EmployeeDTO(x));
	}

	@Transactional
	public EmployeeDTO insert(EmployeeDTO dto) {
		Employee entity = new Employee();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new EmployeeDTO(entity);
	}
	
	private void copyDtoToEntity(EmployeeDTO dto, Employee entity) {
		Department department = departmentRepository.findById(dto.getDepartmentId())
		        .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
		
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setDepartment(department);;
	}

}
