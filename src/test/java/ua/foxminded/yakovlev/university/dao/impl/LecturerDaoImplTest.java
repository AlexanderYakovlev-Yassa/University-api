package ua.foxminded.yakovlev.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ua.foxminded.yakovlev.university.dbconnector.JdbcTemplateFactory;
import ua.foxminded.yakovlev.university.entity.Position;
import ua.foxminded.yakovlev.university.entity.Lecturer;
import ua.foxminded.yakovlev.university.mapper.LecturerMapper;
import ua.foxminded.yakovlev.university.testutil.TestDatabaseGenerator;
import ua.foxminded.yakovlev.university.util.FileReader;

class LecturerDaoImplTest {

	private static TestDatabaseGenerator generator;
	private static LecturerDaoImpl dao;

	@BeforeAll
	static void initTestCase() {
		FileReader fileReader = new FileReader();
		JdbcTemplateFactory jdbcTemplateFactory = new JdbcTemplateFactory("university_db");
		JdbcTemplate jdbcTemplate = jdbcTemplateFactory.getJdbcTemplate();
		RowMapper<Lecturer> rowMapper = new LecturerMapper();
		ScriptExecutor scriptExecutor = new ScriptExecutor(jdbcTemplate);
		generator = new TestDatabaseGenerator(fileReader, scriptExecutor);
		dao = new LecturerDaoImpl(jdbcTemplate, rowMapper);
	}

	@BeforeEach
	void initDB() {
		generator.generate();
	}
	
	@Test
	void findAllShouldReturnCertainListOfLecturers() {
		
		List<Lecturer> expected = getAllLecturers();
		List<Lecturer> actual = dao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void findByIdShouldReturnCertainLecturer() {
		
		Lecturer expected = getAllLecturers().get(1);
		Lecturer actual = dao.findById(2L);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void deleteShouldDeleteCertainLecturer() {
		
		List<Lecturer> expected = getAllLecturers();
		expected.remove(3);
		
		dao.delete(4L);
		
		List<Lecturer> actual = dao.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void findByPositionIdShouldReturnCertainListOfLecturers() {
		
		List<Lecturer> expected = getAllLecturers();
		expected.remove(3);
		expected.remove(2);
		
		List<Lecturer> actual = dao.findByPositionId(7L);
		
		assertEquals(expected, actual);
	}
	
	
	@Test
	void saveShouldSaveCertainLecturer() {
		
		List<Lecturer> expected = getAllLecturers();
		Lecturer newLecturer = getLecturer(3L, "ASSOCIATE_PROFESSOR", 10L, 5L, "Новый", "Лектор");
		expected.add(newLecturer);
		Lecturer lecturerToAdd = getLecturer(3L, "ASSOCIATE_PROFESSOR", 10L, 5L, "Новый", "Лектор");
		
		dao.save(lecturerToAdd);
		
		List<Lecturer> actual = dao.findAll();
		
		assertEquals(expected, actual);
	}
	
	
	@Test
	void updateShouldUpdateCertainFieldsOfLecturer() {
		
		Lecturer expected = getAllLecturers().get(1);
		expected.setFirstName("Измененный");
		expected.setLastName("Студент");
		Position newPosition = getAllLecturers().get(3).getPosition();
		expected.setPosition(newPosition);
		
		dao.update(expected);
		
		Lecturer actual = dao.findById(expected.getLecturerId());
		
		assertEquals(expected, actual);
	}
	
	@Test
	void updateShouldReturnUpdatedLecturer() {
		
		Lecturer newLecturer = getAllLecturers().get(1);
		newLecturer.setFirstName("Измененный");
		newLecturer.setLastName("Студент");
		Position newPosition = getAllLecturers().get(3).getPosition();
		newLecturer.setPosition(newPosition);
		
		Lecturer expected = dao.update(newLecturer);
		
		Lecturer actual = dao.findById(newLecturer.getLecturerId());
		
		assertEquals(expected, actual);
	}
	
	List<Lecturer> getAllLecturers() {
		
		List<Lecturer> allLecturers = new ArrayList<>();
		
		allLecturers.add(getLecturer(7L, "INSTRUCTOR", 6L, 1L, "Андрей", "Аксенов"));
		allLecturers.add(getLecturer(7L, "INSTRUCTOR", 7L, 2L, "Мирон", "Давыдов"));
		allLecturers.add(getLecturer(8L, "RESEARCH_ASSOCIATE", 8L, 3L, "Владимир", "Ойстрах"));
		allLecturers.add(getLecturer(3L, "ASSOCIATE_PROFESSOR", 9L, 4L, "Cтанислав", "Кудесник"));
		
		return allLecturers;
	}

	Lecturer getLecturer(Long positionId, String positionName, Long personId, Long lecturerId, String firstName, String lastName) {
		
		Lecturer lecturer = new Lecturer();
		Position position = new Position();

		position.setId(positionId);
		position.setName(positionName);
		lecturer.setPersonId(personId);
		lecturer.setLecturerId(lecturerId);
		lecturer.setFirstName(firstName);
		lecturer.setLastName(lastName);
		lecturer.setPosition(position);
		
		return lecturer;
	}
}
