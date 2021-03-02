package ua.foxminded.yakovlev.university.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.yakovlev.university.entity.TimetableRecord;
import ua.foxminded.yakovlev.university.entity.User;
import ua.foxminded.yakovlev.university.repository.TimetableRecordRepository;
import ua.foxminded.yakovlev.university.service.LecturerService;
import ua.foxminded.yakovlev.university.service.StudentService;
import ua.foxminded.yakovlev.university.service.TimetableRecordService;

@Service
@Transactional
public class TimetableRecordServiceImpl extends AbstractServiceJpa<TimetableRecord, Long> implements TimetableRecordService {

	private static final String ENTITY_NAME = "Timetable record";
	
	private final TimetableRecordRepository dao;
	private final LecturerService lecturerService;
	private final StudentService studentService;
	
	public TimetableRecordServiceImpl(TimetableRecordRepository timetableRecordRepository, LecturerService lecturerService, StudentService studentService)  {
		super(timetableRecordRepository);
		this.dao = timetableRecordRepository;
		this.lecturerService = lecturerService;
		this.studentService = studentService;
	}

	@Override
	public List<TimetableRecord> findByPeriodOfTime(LocalDateTime periodStart, LocalDateTime periodFinish) {		
		return dao.findByPeriodOfTime(periodStart, periodFinish);
	}

	@Override
	public List<TimetableRecord> findByLecturer(Long lecturerId, LocalDateTime periodStart, LocalDateTime periodFinish) {		
		return dao.findByLecturer(lecturerId, periodStart, periodFinish);
	}

	@Override
	public List<TimetableRecord> findByGroup(Long studentId, LocalDateTime periodStart, LocalDateTime periodFinish) {
		return dao.findByGroup(studentId, periodStart, periodFinish);
	}

	@Override
	protected String getEntityName() {		
		return ENTITY_NAME;
	}

	@Override
	protected Long getIdentifire(TimetableRecord record) {
		return record.getId();
	}

	@Override
	public List<TimetableRecord> findByUser(User user, LocalDateTime startDate, Long amountOfDays) {
		
		if (user == null) {
			throw new IllegalArgumentException("Can't find the timetable for empty user");
		}
		
		LocalDateTime now = LocalDateTime.now();
		
		if (startDate == null) {
			startDate = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0);
		}		

		if (amountOfDays == null || amountOfDays <= 0) {
			amountOfDays = 1L;
		}
		
		LocalDateTime periodStart = startDate;
		LocalDateTime periodFinish = startDate.plusDays(amountOfDays);		
		
		Long personId = user.getPerson().getPersonId();
		
		if (lecturerService.isLecturer(personId)) {	
			return findByLecturer(personId, periodStart, periodFinish);
		}
		
		if (studentService.isStudent(personId)) {
			Long groupId = studentService.findById(personId).getGroup().getId();
			return findByGroup(groupId, periodStart, periodFinish);
		}		

		return Collections.emptyList();
	}
}
