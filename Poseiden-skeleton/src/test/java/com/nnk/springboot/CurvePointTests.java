package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
public class CurvePointTests {

	@Autowired
	private CurvePointService curvePointService;

	@Test
	public void curvePointTest() {
		CurvePoint curvePoint = new CurvePoint(10, 10d, 30d);

		// Save
		curvePoint = curvePointService.save(curvePoint);
		assertNotNull(curvePoint.getId());
        assertEquals(10, curvePoint.getCurveId());

		// Update
		curvePoint.setCurveId(20);
		curvePoint = curvePointService.save(curvePoint);
        assertEquals(20, curvePoint.getCurveId());

		// Find
		List<CurvePoint> listResult = curvePointService.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = curvePoint.getId();
		curvePointService.delete(curvePoint);
		Optional<CurvePoint> curvePointList = curvePointService.findById(id);
		assertFalse(curvePointList.isPresent());
	}

}
