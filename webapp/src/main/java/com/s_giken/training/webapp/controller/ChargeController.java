package com.s_giken.training.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.s_giken.training.webapp.exception.NotFoundException;
import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;
import com.s_giken.training.webapp.service.ChargeService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

@Controller
@RequestMapping("/charge")
public class ChargeController {
    private final ChargeService chargeService;

    public ChargeController(ChargeService chargeService) {
        this.chargeService = chargeService;
    }

    @GetMapping("/search")
    public String showsearchCondition(Model model) {
        var chargeSearchCondition = new ChargeSearchCondition();
        model.addAttribute("chargeSearchCondition", chargeSearchCondition);
        return "charge_search_condition";
    }

    @PostMapping("/search")
    public String searchAndListing(
            @ModelAttribute("chargeSearchCondition") ChargeSearchCondition chargeSearchCondition,
            Model model) {
        var result = chargeService.findByConditions(chargeSearchCondition);
        model.addAttribute("result", result);
        return "charge_search_result";
    }
}
