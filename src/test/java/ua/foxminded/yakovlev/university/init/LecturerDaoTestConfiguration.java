package ua.foxminded.yakovlev.university.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ua.foxminded.yakovlev.university.dao.LecturerDao;
import ua.foxminded.yakovlev.university.dao.impl.LecturerDaoImpl;
import ua.foxminded.yakovlev.university.dao.impl.ScriptExecutor;
import ua.foxminded.yakovlev.university.mapper.LecturerMapper;
import ua.foxminded.yakovlev.university.util.DatabaseGenerator;
import ua.foxminded.yakovlev.university.util.FileReader;

@Configuration
@ComponentScan("ua.foxminded.yakovlev.university")
@PropertySource("university_db.properties")
public class LecturerDaoTestConfiguration {

	@Value("${driver}")
	private String driver;
	@Value("${url}")
	private String url;
	@Value("${user}")
	private String user;
	@Value("${password}")
	private String password;
	@Value("${database_script_path}")
	private String databaseScriptPath;

	@Bean (name="jdbcTemplate")
	public JdbcTemplate getJdbcTemplate() {

		DriverManagerDataSource driverManagerDataSource = 
				new DriverManagerDataSource(url, user, password);
		driverManagerDataSource.setDriverClassName(driver);
		
		return new JdbcTemplate(driverManagerDataSource);
	}
	
	@Bean (name="scriptExecutor")
	public ScriptExecutor getScriptExecutor(JdbcTemplate jdbcTemplate) {
		return new ScriptExecutor(jdbcTemplate);
	}
	
	@Bean (name="databaseGenerator")
	public DatabaseGenerator getDatabaseGenerator(FileReader fileReader, ScriptExecutor scriptExecutor) {
		return new DatabaseGenerator(fileReader, scriptExecutor, databaseScriptPath);		
	}
	
	@Bean (name="lecturerDao")
	public LecturerDao getLecturerDao(JdbcTemplate jdbcTemplate, LecturerMapper lecturerMapper) {		
		return new LecturerDaoImpl(jdbcTemplate, lecturerMapper);
	}
}