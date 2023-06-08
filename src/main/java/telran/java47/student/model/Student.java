package telran.java47.student.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(of = "id")
public class Student {
	@Id
	int id;
	@Setter
	String name;
	@Setter
	String password;
	Map<String, Integer> scores;

	public Student(int id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
		scores = new HashMap<>();
	}
	
	public boolean addScore(String exam, int score) {
		return scores.put(exam, score) == null;
	}
}
