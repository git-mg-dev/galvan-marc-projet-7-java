package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.service.CurvePointService;
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
public class CurveController {
    @Autowired
    private CurvePointService curvePointService;

    //TODO: remettre [[${#httpServletRequest.remoteUser}]] ligne 20 du template list.html

    @GetMapping("/curvePoint/list")
    public String displayCurvePointList(Model model)
    {
        List<CurvePoint> curvePointList = curvePointService.findAll();
        model.addAttribute("curvePoints", curvePointList);

        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String displayAddCurvePointForm(CurvePoint curvePoint) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String addCurvePoint(@Valid CurvePoint curvePoint, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            curvePoint = curvePointService.save(curvePoint);
            return "redirect:list";
        }

        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String displayUpdateCurvePointForm(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<CurvePoint> optionalCurvePoint = curvePointService.findById(id);
            if(optionalCurvePoint.isPresent()) {
                model.addAttribute("curvePoint", optionalCurvePoint.get());
                return "curvePoint/update";
            } else {
                return "redirect:/curvePoint/list?error";
            }
        } else {
            return "redirect:/curvePoint/list?error";
        }
    }

    @PostMapping("/curvePoint/update")
    public String updateCurvePoint(@Valid CurvePoint curvePoint, BindingResult bindingResult, Model model) {

        if(!bindingResult.hasErrors()) {
            curvePoint = curvePointService.save(curvePoint);
            return "redirect:/curvePoint/list";
        }

        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {

        if(id != null) {
            Optional<CurvePoint> optionalCurvePoint = curvePointService.findById(id);
            if(optionalCurvePoint.isPresent()) {
                curvePointService.delete(optionalCurvePoint.get());
                return "redirect:/curvePoint/list";
            } else {
                return "redirect:/curvePoint/list?error";
            }
        } else {
            return "redirect:/curvePoint/list?error";
        }
    }
}
