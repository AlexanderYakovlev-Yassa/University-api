package ua.foxminded.yakovlev.university.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
	
	public IndexController(ApplicationContext context) {
	}

	@GetMapping()
    public String index() {
        return "index";
    }
	
	@GetMapping("/index")
    public String showIndex() {
        return "index";
    }
}