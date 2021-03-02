package ua.foxminded.yakovlev.university.service;

import java.time.LocalDateTime;
import java.util.List;

import ua.foxminded.yakovlev.university.entity.TimetableRecord;
import ua.foxminded.yakovlev.university.entity.User;

public interface TimetableRecordService extends EntityService<TimetableRecord, Long>{

	List<TimetableRecord> findByPeriodOfTime(LocalDateTime periodStart, LocalDateTime periodFinish);
	
	List<TimetableRecord> findByLecturer(Long lecturerId, LocalDateTime periodStart, LocalDateTime periodFinish);
	List<TimetableRecord> findByGroup(Long groupId, LocalDateTime periodStart, LocalDateTime periodFinish);
	
	List<TimetableRecord> findByUser(User user, LocalDateTime startDate, Long amountOfDays);
}
