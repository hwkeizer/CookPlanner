package nl.cookplanner.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.cookplanner.model.Recipe;
import nl.cookplanner.repositories.RecipeRepository;

@Controller
@RequestMapping("test")
public class TestController extends AbstractController {

	private final RecipeRepository recipeRepository;
	
	public TestController(RecipeRepository recipeRepository) {
		super();
		this.recipeRepository = recipeRepository;
	}

	@GetMapping
	public String showTestPage(Model model) {
		List<Recipe> recipeList = recipeRepository.findAll();
		model.addAttribute("recipeList", recipeList);
		return "test/test-layout";
	}
}
