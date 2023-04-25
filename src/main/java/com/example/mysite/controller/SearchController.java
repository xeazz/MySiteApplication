package com.example.mysite.controller;

import com.example.mysite.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class SearchController {
    public final SearchService service;

    @CrossOrigin
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @CrossOrigin
    @GetMapping("/stats")
    public String userRegistration(@RequestParam(value = "name") String name, Model model) {
        final var ageValue = service.getAge(name);
        final var oldName = service.getOldUser().get().name();
        final var biggestAge = service.getOldUser().get().age();
        final var popularName = service.getPopularPerson().get().getKey();
        final var count = service.getPopularPerson().get().getValue();

        model.addAttribute("name", name);
        model.addAttribute("age", ageValue);
        model.addAttribute("oldName", oldName);
        model.addAttribute("biggestAge", biggestAge);
        model.addAttribute("popularName", popularName);
        model.addAttribute("count", count);
        return "statistics";
    }
}
