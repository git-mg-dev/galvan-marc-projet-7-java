package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
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
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @GetMapping("/rating/list")
    public String displayRatingList(HttpServletRequest httpServletRequest, Model model)
    {
        List<Rating> ratings = ratingService.findAll();
        model.addAttribute("ratings", ratings);
        model.addAttribute("username", httpServletRequest.getRemoteUser());

        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String displayAddRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validateAddRating(@Valid Rating rating, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            rating = ratingService.save(rating);
            return "redirect:list";
        }

        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String displayUpdateRatingForm(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<Rating> optionalRating = ratingService.findById(id);
            if(optionalRating.isPresent()) {
                model.addAttribute("rating", optionalRating.get());
                return "rating/update";
            } else {
                return "redirect:/rating/list?error";
            }
        } else {
            return "redirect:/rating/list?error";
        }
    }

    @PostMapping("/rating/update")
    public String updateRating(@Valid Rating rating, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            rating = ratingService.save(rating);
            return "redirect:/rating/list";
        }

        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<Rating> optionalRating = ratingService.findById(id);
            if(optionalRating.isPresent()) {
                ratingService.delete(optionalRating.get());
                return "redirect:/rating/list";
            } else {
                return "redirect:/rating/list?error";
            }
        } else {
            return "redirect:/rating/list?error";
        }
    }
}
