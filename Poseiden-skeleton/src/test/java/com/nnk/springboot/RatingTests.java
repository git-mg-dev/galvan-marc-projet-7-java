package com.nnk.springboot;

import com.nnk.springboot.domain.Rating;

import com.nnk.springboot.service.RatingService;
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
public class RatingTests {

	@Autowired
	private RatingService ratingService;

	@Test
	public void ratingTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);

		// Save
		rating = ratingService.save(rating);
		assertNotNull(rating.getId());
        assertEquals(10, rating.getOrderNumber());

		// Update
		rating.setOrderNumber(20);
		rating = ratingService.save(rating);
        assertEquals(20, rating.getOrderNumber());

		// Find
		List<Rating> listResult = ratingService.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = rating.getId();
		ratingService.delete(rating);
		Optional<Rating> ratingList = ratingService.findById(id);
		assertFalse(ratingList.isPresent());
	}
}
