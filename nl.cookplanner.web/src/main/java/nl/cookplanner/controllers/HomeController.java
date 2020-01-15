package nl.cookplanner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@GetMapping({"/","index"})
	public String getHome() {
		log.debug("We are going home now!");
		return "index";
	}
	
	@PostMapping({"/","index"})
	public String getHomeByPost() {
		log.debug("We are posting home now!");
		return "index";
	}
}
