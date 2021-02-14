package ua.foxminded.yakovlev.university.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class AuthenticationDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message="validator.message.empty_user_name")
	private String username;
	
	@NotBlank(message="validator.message.empty_password")
	private String password;
}
