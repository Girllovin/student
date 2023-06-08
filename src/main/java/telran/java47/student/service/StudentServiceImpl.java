package telran.java47.student.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java47.student.dao.StudentRepository;
import telran.java47.student.dto.ScoreDto;
import telran.java47.student.dto.StudentCreateDto;
import telran.java47.student.dto.StudentDto;
import telran.java47.student.dto.StudentUpdateDto;
import telran.java47.student.dto.exceptions.StudentNotFoundException;
import telran.java47.student.model.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

	final StudentRepository studentRepository;

	@Override
	public boolean addStudent(StudentCreateDto studentCreateDto) {
		if (studentRepository.findById(studentCreateDto.getId()).isPresent()) {
			return false;
		}
		Student student = new Student(studentCreateDto.getId(), studentCreateDto.getName(),
				studentCreateDto.getPassword());
		studentRepository.save(student);
		return true;
	}

	@Override
	public StudentDto findStudent(int id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException());
		return new StudentDto(student.getId(), student.getName(), student.getScores());
	}

	@Override
	public StudentDto removeStudent(int id) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException());
		studentRepository.deleteById(id);
		return new StudentDto(id, student.getName(), student.getScores());
	}

	@Override
	public StudentCreateDto updateStudent(int id, StudentUpdateDto studentUpdateDto) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException());
		if (studentUpdateDto.getName() != null) {
			student.setName(studentUpdateDto.getName());
		}
		if (studentUpdateDto.getPassword() != null) {
			student.setPassword(studentUpdateDto.getPassword());
		}
		studentRepository.save(student);
		return StudentCreateDto.builder().id(id).name(student.getName()).password(student.getPassword()).build();
	}

	@Override
	public boolean addScore(int id, ScoreDto scoreDto) {
		Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException());
		boolean res = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
		studentRepository.save(student);
		return res;
	}

	
	@Override
	public List<StudentDto> findStudentsByName(String name) {
		return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
				.filter(s -> name.equalsIgnoreCase(s.getName()))
				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores())).collect(Collectors.toList());
	}

	@Override
	public long getStudentsNamesQuantity(List<String> names) {
		return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
				.filter(s -> names.contains(s.getName())).count();
	}

	@Override
	public List<StudentDto> getStudentsByExamMinScore(String exam, int minScore) {
		return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
				.filter(s -> s.getScores().containsKey(exam) && s.getScores().get(exam) > minScore)
				.map(s -> new StudentDto(s.getId(), s.getName(), s.getScores())).collect(Collectors.toList());
	}

}
