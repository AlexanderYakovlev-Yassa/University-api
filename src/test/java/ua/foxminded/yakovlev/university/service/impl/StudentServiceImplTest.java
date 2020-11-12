package ua.foxminded.yakovlev.university.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.foxminded.yakovlev.university.entity.Group;
import ua.foxminded.yakovlev.university.entity.Student;
import ua.foxminded.yakovlev.university.init.StudentDaoTestConfiguration;
import ua.foxminded.yakovlev.university.service.StudentService;
import ua.foxminded.yakovlev.university.util.DatabaseGenerator;

class StudentServiceImplTest {

	private static AnnotationConfigApplicationContext context;
	private static DatabaseGenerator generator;
	private static StudentService service;

	@BeforeAll
	static void initTestCase() {
		context = new AnnotationConfigApplicationContext(StudentDaoTestConfiguration.class);
		generator = context.getBean("databaseGenerator", DatabaseGenerator.class);
		service = context.getBean("studentService", StudentServiceImpl.class);
	}

	@BeforeEach
	void initDB() {
		generator.generate();
	}
	
	@Test
	void findAllShouldReturnCertainListOfStudents() {
		
		List<Student> expected = getAllStudents();
		List<Student> actual = service.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void findByIdShouldReturnCertainStudent() {
		
		Student expected = getAllStudents().get(1);
		Student actual = service.findById(2L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void deleteShouldDeleteCertainStudent() {
		
		List<Student> expected = getAllStudents();
		expected.remove(2);
		
		service.delete(3L);
		
		List<Student> actual = service.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void findByGroupIdShouldReturnCertainListOfStudents() {
		
		List<Student> expected = getAllStudents();
		expected.remove(4);
		expected.remove(3);
		
		List<Student> actual = service.findByGroupId(1L);
		
		assertEquals(expected, actual);
	}
	
	
	@Test
	void saveShouldSaveCertainStudent() {
		
		List<Student> expected = getAllStudents();
		Student newStudent = getStudent(1L, "aa-01", 10L, 6L, "Новый", "Студент");
		expected.add(newStudent);
		Student studentToAdd = getStudent(1L, "aa-01", 0L, 0L, "Новый", "Студент");
		
		service.save(studentToAdd);
		
		List<Student> actual = service.findAll();
		
		assertEquals(expected, actual);
	}
	
	
	@Test
	void updateShouldUpdateCertainFieldsOfStudent() {
		
		Student expected = getAllStudents().get(1);
		expected.setFirstName("Измененный");
		expected.setLastName("Студент");
		Group newGroup = getAllStudents().get(3).getGroup();
		expected.setGroup(newGroup);
		
		service.update(expected);
		
		Student actual = service.findById(expected.getStudentId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void updateShouldReturnUpdatedStudent() {
		
		Student newStudent = getAllStudents().get(1);
		newStudent.setFirstName("Измененный");
		newStudent.setLastName("Студент");
		Group newGroup = getAllStudents().get(3).getGroup();
		newStudent.setGroup(newGroup);
		
		Student expected = service.update(newStudent);
		
		Student actual = service.findById(newStudent.getStudentId());
		
		assertEquals(expected, actual);
	}
	
	List<Student> getAllStudents() {
		
		List<Student> allStudents = new ArrayList<>();
		
		allStudents.add(getStudent(1L, "aa-01", 1L, 1L, "Иван", "Иванов"));
		allStudents.add(getStudent(1L, "aa-01", 2L, 2L, "Петр", "Петров"));
		allStudents.add(getStudent(1L, "aa-01", 3L, 3L, "Сидор", "Сидоров"));
		allStudents.add(getStudent(2L, "aa-02", 4L, 4L, "Егор", "Федотов"));
		allStudents.add(getStudent(2L, "aa-02", 5L, 5L, "Максим", "Конев"));
		
		return allStudents;
	}
	
	Student getStudent(Long groupId, String groupName, Long personId, Long studentId, String firstName, String lastName) {
		
		Student student = new Student();
		Group group = new Group();

		group.setId(groupId);
		group.setName(groupName);
		student.setPersonId(personId);
		student.setStudentId(studentId);
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setGroup(group);
		
		return student;
	}
}