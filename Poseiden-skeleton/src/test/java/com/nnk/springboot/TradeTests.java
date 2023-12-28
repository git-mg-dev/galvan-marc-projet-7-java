package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;

import com.nnk.springboot.service.TradeService;
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
public class TradeTests {

	@Autowired
	private TradeService tradeService;

	@Test
	public void tradeTest() {
		Trade trade = new Trade("Trade Account", "Type");

		// Save
		trade = tradeService.save(trade);
		assertNotNull(trade.getId());
        assertEquals("Trade Account", trade.getAccount());

		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeService.save(trade);
        assertEquals("Trade Account Update", trade.getAccount());

		// Find
		List<Trade> listResult = tradeService.findAll();
        assertFalse(listResult.isEmpty());

		// Delete
		Integer id = trade.getId();
		tradeService.delete(trade);
		Optional<Trade> tradeList = tradeService.findById(id);
		assertFalse(tradeList.isPresent());
	}
}
