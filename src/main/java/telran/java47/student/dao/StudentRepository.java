package telran.java47.student.dao;

import org.springframework.data.repository.CrudRepository;

import telran.java47.student.model.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {
	
}
