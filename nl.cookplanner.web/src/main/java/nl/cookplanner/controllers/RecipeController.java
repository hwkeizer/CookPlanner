package nl.cookplanner.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.Tag;
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.repositories.TagRepository;

@Controller
@RequestMapping("recipe")
@Slf4j
public class RecipeController {

	private final RecipeRepository recipeRepository;
	private final TagRepository tagRepository;
	
	public RecipeController(RecipeRepository recipeRepository, TagRepository tagRepository) {
		this.recipeRepository = recipeRepository;
		this.tagRepository = tagRepository;
	}
	
	@ModelAttribute("allTags")
	public List<Tag> getAllTags() {
		List<Tag> allTags = tagRepository.findAll();
		return allTags;
	}

	@GetMapping("list")
	public String showRecipeList(Model model) {
		List<Recipe> recipeList = recipeRepository.findAll();
		model.addAttribute("recipeList", recipeList);
		return "recipe/list";
	}
	
	@GetMapping("{id}/show")
	public String showRecipeById(@PathVariable String id, Model model) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(Long.valueOf(id));
		if (optionalRecipe.isPresent()) {
			model.addAttribute("recipe", optionalRecipe.get());
		}
		return "recipe/show-details";
	}
	
	@GetMapping("{id}/update") 
	public String showUpdateFormById(@PathVariable String id, Model model) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(Long.valueOf(id));
		if (optionalRecipe.isPresent()) {
			model.addAttribute("recipe", optionalRecipe.get());
		}
		return "recipe/update";
	}
	
	@PostMapping("update")
	public String updateRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {
				log.debug(objectError.toString());
			});
			return "recipe/update";
		}
		log.debug("Recipe to update: {}", recipe);
		Recipe updatedRecipe = recipeRepository.save(recipe);
		return "redirect:/recipe/" + updatedRecipe.getId() + "/show";
	}
}
