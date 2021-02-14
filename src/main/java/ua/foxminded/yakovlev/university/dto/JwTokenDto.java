package ua.foxminded.yakovlev.university.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class JwTokenDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message="validator.message.empty_token")
	private String token;
}
