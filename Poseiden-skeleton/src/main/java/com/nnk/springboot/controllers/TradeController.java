package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
public class TradeController {
    @Autowired
    private TradeService tradeService;

    //TODO: remettre [[${#httpServletRequest.remoteUser}]] ligne 20 du template list.html

    @GetMapping("/trade/list")
    public String displayTradeList(Model model)
    {
        List<Trade> trades = tradeService.findAll();
        model.addAttribute("trades", trades);
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String displayAddTradeForm(Trade trade) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validateAddTrade(@Valid Trade trade, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            trade = tradeService.save(trade);
            return "redirect:list";
        }

        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String displayUpdateTradeForm(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<Trade> optionalTrade = tradeService.findById(id);
            if(optionalTrade.isPresent()) {
                model.addAttribute("trade", optionalTrade.get());
                return "trade/update";
            } else {
                return "redirect:/trade/list?error";
            }
        } else {
            return "redirect:/trade/list?error";
        }
    }

    @PostMapping("/trade/update")
    public String updateTrade(@Valid Trade trade, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            trade = tradeService.save(trade);
            return "redirect:/trade/list";
        }

        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<Trade> optionalTrade = tradeService.findById(id);
            if(optionalTrade.isPresent()) {
                tradeService.delete(optionalTrade.get());
                return "redirect:/trade/list";
            } else {
                return "redirect:/trade/list?error";
            }
        } else {
            return "redirect:/trade/list?error";
        }
    }
}
