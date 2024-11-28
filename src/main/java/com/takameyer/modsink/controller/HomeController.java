package com.takameyer.modsink.controller;

import com.takameyer.modsink.model.Member;
import com.takameyer.modsink.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("member", new Member());
        model.addAttribute("members", memberService.findAll());
        return "index";
    }

    @PostMapping("/register")
    public String register(@Valid Member member, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
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
            model.addAttribute("globalError", "Registration failed: " + e.getMessage());
            model.addAttribute("members", memberService.findAll());
            return "index";
        }
    }
}
