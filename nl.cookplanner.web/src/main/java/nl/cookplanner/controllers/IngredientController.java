package nl.cookplanner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.services.IngredientNameService;
import nl.cookplanner.services.MeasureUnitService;
import nl.cookplanner.services.RecipeService;

@Controller
@RequestMapping("ingredient")
@Slf4j
public class IngredientController {

	private final RecipeService recipeService;
	private final MeasureUnitService measureUnitService;
	private final IngredientNameService ingredientNameService;

	public IngredientController(RecipeService recipeService, MeasureUnitService measureUnitService,
			IngredientNameService ingredientNameService) {
		this.recipeService = recipeService;
		this.measureUnitService = measureUnitService;
		this.ingredientNameService = ingredientNameService;
	}

	@GetMapping("/create/{recipeId}")
	public String createIngredient(@PathVariable String recipeId, Model model) {
		
//		Recipe recipe = recipeService.findRecipeById(Long.valueOf(recipeId));
//		if (recipe == null) {
//			// TODO: add correct error handling
//			log.error("Recipe with id {} not found!", recipeId);
//			return "recipe/list";
//		}
		
		Ingredient ingredient = new Ingredient();
		ingredient.setMeasureUnit(new MeasureUnit());
		ingredient.setName(new IngredientName());
		model.addAttribute("ingredient", ingredient);
		model.addAttribute("recipeId", recipeId);
		
		model.addAttribute("measureUnitList", measureUnitService.findAllMeasureUnits());
		model.addAttribute("ingredientNameList", ingredientNameService.findAllIngredientNames());
		
		return "ingredient/create";
	}
	
	// TODO: Look for a more elegant implementation
	@PostMapping("create/{recipeId}")
	public String saveIngredient(@PathVariable String recipeId, @ModelAttribute Ingredient ingredient) {

//			Ingredient updatedIngredient = ingredientService.updateIngredientCommand(ingredientCommand);		
		log.debug("Updated ingredient: {}", ingredient);
		log.debug("For recipe: {}", recipeId);
		return "";
	}			
}

