package ua.foxminded.yakovlev.university.controller.api;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import ua.foxminded.yakovlev.university.dto.AuthenticationDto;
import ua.foxminded.yakovlev.university.dto.JwTokenDto;
import ua.foxminded.yakovlev.university.service.impl.UserService;
import ua.foxminded.yakovlev.university.util.JwtUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/authenticate", produces = "application/json")
public class ApiAuthenticationController {
	
	private final JwtUtil jwtUtil;
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	
	@PostMapping
    public ResponseEntity<JwTokenDto> authorise(@Valid@RequestBody AuthenticationDto autenticationDto) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(autenticationDto.getUsername(), autenticationDto.getPassword()));
				
        UserDetails userDetails = userService.loadUserByUsername(autenticationDto.getUsername());
        JwTokenDto token = new JwTokenDto();
        token.setToken(jwtUtil.generateToken(userDetails));
        
        return ResponseEntity.ok(token);
    }
}