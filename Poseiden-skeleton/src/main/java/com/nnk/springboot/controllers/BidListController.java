package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.service.BidListService;
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
public class BidListController {
    @Autowired
    private BidListService bidListService;

    //TODO: remettre [[${#httpServletRequest.remoteUser}]] ligne 20 du template list.html

    @GetMapping("/bidList/list")
    public String displayBidList(Model model)
    {
        List<BidList> bidList = bidListService.findAll();
        model.addAttribute("bidLists", bidList);

        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String displayAddBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String addBid(@Valid BidList bid, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            bid = bidListService.save(bid);
            return "redirect:list";
        }

        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String displayUpdateBidForm(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<BidList> optionalBid = bidListService.findById(id);
            if(optionalBid.isPresent()) {
                model.addAttribute("bidList", optionalBid.get());
                return "bidList/update";
            } else {
                return "redirect:/bidList/list?error";
            }
        } else {
            return "redirect:/bidList/list?error";
        }
    }

    @PostMapping("/bidList/update")
    public String updateBid(@Valid BidList bid, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            bid = bidListService.save(bid);
            return "redirect:/bidList/list";
        }

        model.addAttribute("bidList", bid);
        return "bidList/update";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<BidList> optionalBid = bidListService.findById(id);
            if(optionalBid.isPresent()) {
                bidListService.delete(optionalBid.get());
                return "redirect:/bidList/list";
            } else {
                return "redirect:/bidList/list?error";
            }
        } else {
            return "redirect:/bidList/list?error";
        }
    }
}
