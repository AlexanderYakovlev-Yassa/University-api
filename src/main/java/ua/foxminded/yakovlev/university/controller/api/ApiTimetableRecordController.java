package ua.foxminded.yakovlev.university.controller.api;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import ua.foxminded.yakovlev.university.dto.TimetableRecordDto;
import ua.foxminded.yakovlev.university.entity.TimetableRecord;
import ua.foxminded.yakovlev.university.entity.User;
import ua.foxminded.yakovlev.university.mapper.TimetableRecordMapper;
import ua.foxminded.yakovlev.university.service.TimetableRecordService;
import ua.foxminded.yakovlev.university.service.impl.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timetableRecords")
public class ApiTimetableRecordController {
	
	private final TimetableRecordService timetableRecordService;
	private final UserService userService;
	private final TimetableRecordMapper timetableRecordMapper;
	@Qualifier(value="messageSource")
	private final ResourceBundleMessageSource messageSource;
	
	@GetMapping
	@PreAuthorize("hasAuthority('READ_TIMETABLE')")
    public ResponseEntity<List<TimetableRecordDto>> findAll() {		
        return ResponseEntity.ok(timetableRecordMapper.toTimetableRecordDtos(timetableRecordService.findAll()));
    }
	
	@PostMapping
	@PreAuthorize("hasAuthority('MANAGE_TIMETABLE')")
    public ResponseEntity<TimetableRecordDto> create(@Valid@RequestBody TimetableRecordDto timetableRecordDto) {        
        return ResponseEntity.status(HttpStatus.CREATED).body(timetableRecordMapper
        		.toTimetableRecordDto(timetableRecordService.save(timetableRecordMapper.toTimetableRecord(timetableRecordDto))));
    }
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('READ_TIMETABLE')")
    public ResponseEntity<TimetableRecordDto> findById(@PathVariable Long id) {               
        return ResponseEntity.ok(timetableRecordMapper.toTimetableRecordDto(timetableRecordService.findById(id)));
    }
	
	@CrossOrigin(methods = RequestMethod.PUT)
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('MODIFY_TIMETABLE')")
    public ResponseEntity<TimetableRecordDto> update(@PathVariable Long id, @Valid@RequestBody TimetableRecordDto timetableRecordDto) {
		
        TimetableRecord timetableRecord = timetableRecordMapper.toTimetableRecord(timetableRecordDto);
        timetableRecord.setId(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(timetableRecordMapper.toTimetableRecordDto(timetableRecordService.update(timetableRecord)));
    }

	@CrossOrigin(methods = RequestMethod.DELETE)
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('MANAGE_TIMETABLE')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        timetableRecordService.delete(id);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
	
	@GetMapping("/date")
	@PreAuthorize("hasAuthority('READ_TIMETABLE')")
    public ResponseEntity<List<TimetableRecordDto>> findByDate(
    		@RequestParam(name="start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam(name="end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    		) {
        return ResponseEntity.ok(timetableRecordMapper.toTimetableRecordDtos(timetableRecordService.findByPeriodOfTime(start, end)));
    }
	
	@GetMapping("/date/group")
	@PreAuthorize("hasAuthority('READ_TIMETABLE')")
    public ResponseEntity<List<TimetableRecordDto>> findByGroup(
    		@RequestParam(name="start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam(name="end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
    		@RequestParam(name="group-id") Long groupId
    		){
        return ResponseEntity.ok(timetableRecordMapper.toTimetableRecordDtos(timetableRecordService.findByGroup(groupId, start, end)));
    }
	
	@GetMapping("/date/lecturer")
	@PreAuthorize("hasAuthority('READ_TIMETABLE')")
    public ResponseEntity<List<TimetableRecordDto>> findByLecturer(
    		@RequestParam(name="start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
    		@RequestParam(name="end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
    		@RequestParam(name="lecturer-id") Long lecturerId
    		) {		
        return ResponseEntity.ok(timetableRecordMapper.toTimetableRecordDtos(timetableRecordService.findByLecturer(lecturerId, start, end)));
    }
	
	@GetMapping("/personal")
	@PreAuthorize("hasAuthority('READ_TIMETABLE')")
    public ResponseEntity<List<TimetableRecordDto>> showPersonalTimetable(
    		@RequestParam(name="start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, 
    		@RequestParam(name="numberOfDays", required = false) Long numberOfDays, 
    		Principal principal) {
		
		if (principal == null) {
			throw new IllegalArgumentException("Undefined user");
		}
		
		User activeUser = userService.findByUsername(principal.getName());
		
        return ResponseEntity.ok(timetableRecordMapper.toTimetableRecordDtos(timetableRecordService.findByUser(activeUser, start, numberOfDays)));
    }
}