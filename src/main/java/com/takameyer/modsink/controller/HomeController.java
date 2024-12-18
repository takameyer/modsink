package com.takameyer.modsink.controller;

import com.takameyer.modsink.model.Member;
import com.takameyer.modsink.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private MemberService memberService;

    /**
     * Home page
     * @param model The model
     * @return The view name
     */
    @GetMapping("/")
    @Operation(summary = "Home page")
    public String home(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("members", memberService.findAll());
        return "index";
    }

    /**
     * Register a new member
     * @param member The member
     * @param bindingResult The binding result
     * @param model The model
     * @return The view name
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new member")
    public String register(@Valid Member member, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("Violations found: " + bindingResult.getAllErrors().size());
            // If there are validation errors, return to the form with error messages
            model.addAttribute("members", memberService.findAll());
            model.addAttribute("org.springframework.validation.BindingResult.member", bindingResult);
            return "index";
        }

        try {
            memberService.register(member);
            return "redirect:/";
        } catch (Exception e) {
            // Handle any service-level exceptions
            log.info("Violations found: " + e.getMessage());
            model.addAttribute("globalError", "Registration failed: " + e.getMessage());
            model.addAttribute("members", memberService.findAll());
            return "index";
        }
    }
}
