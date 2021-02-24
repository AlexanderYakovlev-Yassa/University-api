package ua.foxminded.yakovlev.university;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class SystemTest {
	
	@Autowired
	private MockMvc mockMvc;
		
	@Test
	@WithMockUser(username = "ram", authorities={"MANAGE_COURSE", "READ_COURSE", "MODIFY_COURSE"})
	void shoudReturnResponseWithStatusOk() throws Exception {
		
		this.mockMvc.perform(get("/api/courses"))
		.andExpect(status().isOk())
		.andExpect(content()
		.contentType(MediaType.APPLICATION_JSON));
	}
}