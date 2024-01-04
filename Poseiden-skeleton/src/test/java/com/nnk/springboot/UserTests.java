package com.nnk.springboot;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.UserForm;
import com.nnk.springboot.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
		UserForm userForm = new UserForm("azertyt", "Azerty Test", "Password#1", "USER");

		// Save
		User user = userService.save(userForm);
		assertNotNull(user.getId());
        assertEquals(userForm.getUsername(), user.getUsername());

		// Update
		userForm.setId(user.getId());
		userForm.setUsername("Uiop");
		userForm.setPassword("Password#2");
		user = userService.save(userForm);
        assertEquals(userForm.getUsername(), user.getUsername());

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
