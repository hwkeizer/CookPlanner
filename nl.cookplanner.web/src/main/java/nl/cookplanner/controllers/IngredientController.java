package nl.cookplanner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.services.RecipeService;

@Controller
@RequestMapping("ingredient")
@Slf4j
public class IngredientController {

	private final RecipeService recipeService;
	
	public IngredientController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@GetMapping("/create/{recipeId}")
	public String createIngredient(@PathVariable String recipeId, Model model) {
		
		Recipe recipe = recipeService.findRecipeById(Long.valueOf(recipeId));
		if (recipe == null) {
			// TODO: add correct error handling
			log.error("Recipe with id {} not found!", recipeId);
		}
		
		Ingredient ingredient = new Ingredient();
		ingredient.setRecipe(recipe);
		model.addAttribute("ingredient", ingredient);
		
		ingredient.setMeasureUnit(new MeasureUnit());
		ingredient.setName(new IngredientName());
		
//		model.addAttribute("measureUnitList", measureUnitService.listAllMeasureUnits());
		
		return "ingredient/create";
	}
}
