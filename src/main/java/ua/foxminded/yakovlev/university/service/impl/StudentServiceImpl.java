package ua.foxminded.yakovlev.university.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.yakovlev.university.entity.Group;
import ua.foxminded.yakovlev.university.entity.Student;
import ua.foxminded.yakovlev.university.repository.StudentRepository;
import ua.foxminded.yakovlev.university.service.GroupService;
import ua.foxminded.yakovlev.university.service.StudentService;

@Service
@Transactional
public class StudentServiceImpl extends AbstractServiceJpa<Student, Long> implements StudentService {

	private static final String ENTITY_NAME = "Student";
	
	private final StudentRepository studentDao;
	private final GroupService groupService;
	
	public StudentServiceImpl(StudentRepository studentDao, GroupService groupService)  {
		super(studentDao);
		this.studentDao = studentDao;
		this.groupService = groupService;
	}

	@Override
	public List<Student> findByGroupId(Long groupId) {
		return studentDao.findByGroupId(groupId);
	}

	@Override
	public Student addGroup(Long studentId, Long groupId) {
		
		Student student = findById(studentId);
		Group group = groupService.findById(groupId);
		
		if (student.getGroup() != null) {
			throw new IllegalArgumentException("A student with ID=" + studentId + " already has a group with ID=" + groupId + "!");
		}
		
		student.setGroup(group);
		
		return update(student);
	}

	@Override
	public Student removeGroup(Long studentId, Long groupId) {
		
		Student student = findById(studentId);
		
		if (student.getGroup() == null || student.getGroup().getId() != groupId) {
			throw new IllegalArgumentException("Group with ID=" + groupId + " don't have a student with ID=" + studentId + "!");
		}
		
		student.setGroup(null);
		
		return update(student);
	}

	@Override
	public List<Student> findStudentsWithoutGroup() {
		return studentDao.findByGroupId(null);
	}

	@Override
	protected String getEntityName() {		
		return ENTITY_NAME;
	}

	@Override
	protected Long getIdentifire(Student student) {
		return student.getPersonId();
	}

	@Override
	public boolean isStudent(Long personId) {
		return studentDao.findById(personId).orElse(null) != null;
	}
}
