package ru.itis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itis.dto.QueueDto;
import ru.itis.services.QueueService;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String getIndexPage() {
        return "index";
    }

}
