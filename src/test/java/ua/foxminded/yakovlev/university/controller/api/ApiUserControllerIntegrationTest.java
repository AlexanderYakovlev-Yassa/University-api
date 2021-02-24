package ua.foxminded.yakovlev.university.controller.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.foxminded.yakovlev.university.entity.User;
import ua.foxminded.yakovlev.university.exception.NotFoundException;
import ua.foxminded.yakovlev.university.mapper.UserMapper;
import ua.foxminded.yakovlev.university.service.AuthorityService;
import ua.foxminded.yakovlev.university.service.CourseService;
import ua.foxminded.yakovlev.university.service.GroupService;
import ua.foxminded.yakovlev.university.service.LecturerService;
import ua.foxminded.yakovlev.university.service.PositionService;
import ua.foxminded.yakovlev.university.service.RoleService;
import ua.foxminded.yakovlev.university.service.StudentService;
import ua.foxminded.yakovlev.university.service.TimetableRecordService;
import ua.foxminded.yakovlev.university.service.impl.UserService;
import ua.foxminded.yakovlev.university.util.UserGenerator;

@WebMvcTest(ApiUserController.class)
class ApiUserControllerIntegrationTest {

	@MockBean
	private UserService userService;
	
	@MockBean
	private GroupService gs;
	@MockBean
	private LecturerService ls;
	@MockBean
	private PositionService ps;
	@MockBean
	private StudentService ss;
	@MockBean
	private TimetableRecordService ts;
	@MockBean
	private CourseService cs;
	@MockBean
	private RoleService rs;
	@MockBean
	private AuthorityService as;
	
	@Autowired
	private MockMvc mockMvc;
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	private UserGenerator userGenerator;
	
	@BeforeEach
	private void init() {
		objectMapper = new ObjectMapper();
		userGenerator = new UserGenerator();
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"READ_USER"})
	void findAllShouldReturnAllUsers() throws Exception {
		
		List<User> userList = userGenerator.getUserList();
		
		when(userService.findAll()).thenReturn(userList);
		
		this.mockMvc.perform(get("/api/users"))
		   .andExpect(status().isOk())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		   .andExpect(content().json(objectMapper.writeValueAsString(userMapper.toUserDtos(userList))));
	}
	
	@Test
	@WithMockUser(username = "rrr", authorities={"MANAGE_USER"})
	void createShouldSaveUserIfitValidAndReturnSavedUser() throws Exception {
		
		User user = userGenerator.getUserList().get(0);
		String jsonUserDto = objectMapper.writeValueAsString(userMapper.toUserDto(user));
		
		when(userService.save(any(User.class))).thenReturn(user);
				
		this.mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
	            .content(jsonUserDto))
		   .andExpect(status().isCreated())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		   .andExpect(content().json(jsonUserDto));
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"MANAGE_USER"})
	void createShouldReturnStatusIfUserIsInvalid() throws Exception {
		
		User user = userGenerator.getUserList().get(0);
		user.setName("");
		String jsonUserDto = objectMapper.writeValueAsString(userMapper.toUserDto(user));
		
		when(userService.save(user)).thenReturn(user);
				
		this.mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
	            .content(jsonUserDto))
		   .andExpect(status().isBadRequest())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		verify(userService, never()).save(user);
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"READ_USER"})
	void findByIdShouldReturnUserIfSuchUserExists() throws Exception {

		User user = userGenerator.getUserList().get(0);
		Long id = 1L;
		String jsonUserDto = objectMapper.writeValueAsString(userMapper.toUserDto(user));
		
		when(userService.findById(id)).thenReturn(user);
				
		this.mockMvc.perform(get("/api/users/{id}", id))
		   .andExpect(status().isOk())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		   .andExpect(content().json(jsonUserDto));
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"READ_USER"})
	void findByIdShouldReturnStatusNotFoundIfSuchUserDoesNotExist() throws Exception {

		Long id = 1L;
		
		when(userService.findById(id)).thenThrow(NotFoundException.class);
				
		this.mockMvc.perform(get("/api/users/{id}", id))
		   .andExpect(status().isNotFound())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"MODIFY_USER"})
	void updateShouldReturnUpdatedUserDtoIfSuchUserExistsAndValid() throws Exception {

		User user = userGenerator.getUserList().get(0);
		Long id = user.getId();
		String jsonUserDto = objectMapper.writeValueAsString(userMapper.toUserDto(user));
		
		when(userService.update(any(User.class))).thenReturn(user);
				
		this.mockMvc.perform(put("/api/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonUserDto))
		   .andExpect(status().isAccepted())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		   .andExpect(content().json(jsonUserDto));
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"MODIFY_USER"})
	void updateShouldReturnStatusBadRequestIfSuchUserExistsButInvalid() throws Exception {

		User user = userGenerator.getUserList().get(0);
		Long id = user.getId();
		user.setName("");
		String jsonUserDto = objectMapper.writeValueAsString(userMapper.toUserDto(user));
		
		when(userService.update(user)).thenReturn(user);
				
		this.mockMvc.perform(put("/api/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonUserDto))
		   .andExpect(status().isBadRequest())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		verify(userService, never()).update(user);
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"MODIFY_USER"})
	void updateShouldReturnStatusNotFoundIfSuchUserDoesNotExist() throws Exception {

		User user = userGenerator.getUserList().get(0);
		Long id = user.getId();
		String jsonUserDto = objectMapper.writeValueAsString(userMapper.toUserDto(user));
		
		when(userService.update(any(User.class))).thenThrow(NotFoundException.class);
				
		this.mockMvc.perform(put("/api/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonUserDto))
		   .andExpect(status().isNotFound())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"MANAGE_USER"})
	void deleteShouldReturnStatusAcceptedIfSuchUserExists() throws Exception {
		
		Long id = 1L;
		
		this.mockMvc.perform(delete("/api/users/{id}", id))
		   .andExpect(status().isAccepted());
		
		verify(userService).delete(id);
	}
	
	@Test
	@WithMockUser(username = "user", authorities={"MANAGE_USER"})
	void deleteShouldReturnStatusNotFoundIfSuchUserDoesNotExist() throws Exception {
		
		Long id = 1L;
		
		doThrow(NotFoundException.class).when(userService).delete(id);
		
		this.mockMvc.perform(delete("/api/users/{id}", id))
		   .andExpect(status().isNotFound());
	}
}
