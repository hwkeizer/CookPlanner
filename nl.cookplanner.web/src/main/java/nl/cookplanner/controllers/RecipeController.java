package nl.cookplanner.controllers;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.Tag;
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.repositories.TagRepository;
import nl.cookplanner.services.IngredientService;

@Controller
@Slf4j
public class RecipeController extends AbstractController {

	private final RecipeRepository recipeRepository;
	private final TagRepository tagRepository;
	private final IngredientService ingredientService;
	
	public RecipeController(RecipeRepository recipeRepository, TagRepository tagRepository,
			IngredientService ingredientService) {
		this.recipeRepository = recipeRepository;
		this.tagRepository = tagRepository;
		this.ingredientService = ingredientService;
	}
	
	@ModelAttribute("allTags")
	public List<Tag> getAllTags() {
		List<Tag> allTags = tagRepository.findAll();
		return allTags;
	}

	@GetMapping({"recipe/list", "home", "/"})
	public String showRecipeList(Model model) {
		List<Recipe> recipeList = recipeRepository.findAll();
		model.addAttribute("recipeList", recipeList);
		return "recipe/list";
	}
	
	@GetMapping("recipe/{id}/show")
	public String showRecipeById(@PathVariable String id, Model model) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(Long.valueOf(id));
		if (optionalRecipe.isPresent()) {
			model.addAttribute("recipe", optionalRecipe.get());
		}
		return "recipe/show-details";
	}
	
	@GetMapping("recipe/{id}/update") 
	public String showUpdateFormById(@PathVariable String id, Model model) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(Long.valueOf(id));
		if (optionalRecipe.isPresent()) {
			model.addAttribute("recipe", optionalRecipe.get());
		}
		return "recipe/update";
	}
	
	@PostMapping("recipe/update")
	public String updateRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {
				log.debug(objectError.toString());
			});
			return "recipe/update";
		}
		// Ingredients are updated separately and are fetched here
		// TODO find better solution for this
		recipe.setIngredients(ingredientService.findAllIngredientsForRecipe(recipe.getId()));
		recipeRepository.save(recipe);
		return "redirect:/recipe/" + recipe.getId() + "/show";
	}
}
