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
import nl.cookplanner.services.RecipeService;

@Controller
@Slf4j
public class RecipeController extends AbstractController {

	private final RecipeRepository recipeRepository;
	private final RecipeService recipeService;
	private final TagRepository tagRepository;
	private final IngredientService ingredientService;
	
	public RecipeController(RecipeRepository recipeRepository, RecipeService recipeService, TagRepository tagRepository,
			IngredientService ingredientService) {
		this.recipeRepository = recipeRepository;
		this.recipeService = recipeService;
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
		Recipe recipe = recipeService.findRecipeById(Long.valueOf(id));
		model.addAttribute("recipe", recipe);
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
	
	@GetMapping("recipe/create")
	public String getNewRecipeForm(Model model) {	
		model.addAttribute("recipe", new Recipe());
		return "recipe/create";
	}
	
	@PostMapping("recipe/create")
	public String createRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {
				log.debug(objectError.toString());
			});
			return "recipe/create";
		}
		Recipe savedRecipe = recipeService.createRecipe(recipe);
		return "redirect:/recipe/" + savedRecipe.getId() + "/update";
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
	
	// TODO: turn this into a Delete mapping
	@GetMapping("recipe/{id}/delete")
	public String deleteRecipeById(@PathVariable String id) {
		log.debug("Deleting id: {}", id);
		
		recipeService.deleteRecipeById(Long.valueOf(id));
		return "redirect:/";
	}
}
