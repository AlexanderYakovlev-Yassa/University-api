package ua.foxminded.yakovlev.university.servise;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.foxminded.yakovlev.university.exception.NotFoundException;
import ua.foxminded.yakovlev.university.repository.UserRepository;
import ua.foxminded.yakovlev.university.service.impl.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiseIntegrationTest {
	
	UserService service;
	
	@Mock
	UserRepository repository;
	
    
	@BeforeEach
    public void init() {
    	service = new UserService(repository);
    }
	
	@Test
	void findByIdShouldThrowNotFoundExceptionWhenSuchUserNotFound() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(NotFoundException.class, () -> service.findById(99L));
	}
}