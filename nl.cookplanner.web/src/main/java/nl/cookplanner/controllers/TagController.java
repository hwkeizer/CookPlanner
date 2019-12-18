package nl.cookplanner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TagController {

	@RequestMapping({"/tags/list"})
	public String listAllTags() {
		return "tags/tag-all";
	}
}
