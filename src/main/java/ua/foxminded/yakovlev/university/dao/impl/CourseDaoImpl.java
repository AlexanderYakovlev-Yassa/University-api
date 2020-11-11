package ua.foxminded.yakovlev.university.dao.impl;

import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.foxminded.yakovlev.university.dao.AbstractDao;
import ua.foxminded.yakovlev.university.dao.CourseDao;
import ua.foxminded.yakovlev.university.entity.Course;
import ua.foxminded.yakovlev.university.exception.NotFoundException;

@Component
public class CourseDaoImpl extends AbstractDao<Course, Long> implements CourseDao {
	
	private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

	private static final String FIND_ALL = "SELECT * FROM public.courses;";
	private static final String SAVE = "INSERT INTO public.courses(course_name, course_description) VALUES (?, ?);";
	private static final String FIND_BY_ID = "SELECT * FROM public.courses WHERE course_id = ?;";
	private static final String UPDATE = "UPDATE public.courses "
			+ "SET course_name=?, course_description=? "
			+ "WHERE course_id=?;";
	private static final String DELETE = "DELETE FROM public.courses WHERE course_id = ?;";
	private static final String FIND_BY_COURSE_NAME = "SELECT * FROM public.courses WHERE course_name = ?;";
	private static final String ID_KEY = "course_id";
	
	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Course> courseMapper;
		
	@Autowired
	public CourseDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Course> courseMapper) {
		super(jdbcTemplate, courseMapper, FIND_ALL, FIND_BY_ID, DELETE);
		this.jdbcTemplate = jdbcTemplate;
		this.courseMapper = courseMapper;
	}

	@Override
	public Course findCourseByName(String courseName) {
		
		try {
			return jdbcTemplate.queryForObject(FIND_BY_COURSE_NAME, courseMapper, courseName);
		} catch (EmptyResultDataAccessException e) {
			logger.warn("Course is not found", e);
			throw new NotFoundException("Course is not found");
		}		
	}

	@Override
	public PreparedStatementCreator getPreparedStatementCreatorForSave(Course course) {
				
		return connection -> {
	        PreparedStatement ps = connection
	                .prepareStatement(SAVE, new String[] { ID_KEY });
	                ps.setString(1, course.getName());
	                ps.setString(2, course.getDescription());
	                return ps;
	              };
	}

	@Override
	public PreparedStatementCreator getPreparedStatementCreatorForUpdate(Course course) {
		
		return connection -> {
	        PreparedStatement ps = connection
	                .prepareStatement(UPDATE, new String[] { ID_KEY });
	                ps.setString(1, course.getName());
	                ps.setString(2, course.getDescription());
	                ps.setLong(3, course.getId());
	                return ps;
	              };
	}
}
