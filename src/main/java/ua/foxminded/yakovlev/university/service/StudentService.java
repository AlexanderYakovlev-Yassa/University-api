package ua.foxminded.yakovlev.university.service;

import java.util.List;

import ua.foxminded.yakovlev.university.entity.Student;

public interface StudentService extends EntityService<Student, Long>{

	List<Student> findByGroupId(Long groupId);
	List<Student> findStudentsWithoutGroup();
	Student addGroup(Long studentId, Long groupId);
	Student removeGroup(Long studentId, Long groupId);
	boolean isStudent(Long personId);
}
