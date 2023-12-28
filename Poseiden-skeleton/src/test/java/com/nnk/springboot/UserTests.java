package com.nnk.springboot;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserTests {

	@Autowired
	private UserService userService;

	@Test
	public void userTest() {
		User user = new User("Azerty", "Test", "password", "USER");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));

		// Save
		user = userService.save(user);
		assertNotNull(user.getId());
        assertEquals("Azerty", user.getUsername());

		// Update
		user.setUsername("Uiop");
		user = userService.save(user);
        assertEquals("Uiop", user.getUsername());

		// Find
		List<User> listResult = userService.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = user.getId();
		userService.delete(user);
		Optional<User> user1 = userService.findById(id);
		assertFalse(user1.isPresent());
	}
}
