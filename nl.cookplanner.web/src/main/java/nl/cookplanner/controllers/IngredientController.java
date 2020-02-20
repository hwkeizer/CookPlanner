package nl.cookplanner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.services.IngredientNameService;
import nl.cookplanner.services.IngredientService;
import nl.cookplanner.services.MeasureUnitService;
import nl.cookplanner.services.RecipeService;

@Controller
@RequestMapping("ingredient")
@Slf4j
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final MeasureUnitService measureUnitService;
	private final IngredientNameService ingredientNameService;
	
	public IngredientController(RecipeService recipeService, IngredientService ingredientService,
			MeasureUnitService measureUnitService, IngredientNameService ingredientNameService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.measureUnitService = measureUnitService;
		this.ingredientNameService = ingredientNameService;
	}

	@GetMapping("/list/{recipeId}")
	public String showIngredientListForRecipe(@PathVariable String recipeId, Model model) {
		Recipe recipe = recipeService.findRecipeById(Long.valueOf(recipeId));
		model.addAttribute("ingredientList", recipe.getIngredients());
		model.addAttribute("recipeId", recipeId);
		return "ingredient/list";
	}

	@GetMapping("/create/{recipeId}")
	public String createIngredient(@PathVariable String recipeId, Model model) {
		
		Ingredient ingredient = new Ingredient();
		ingredient.setMeasureUnit(new MeasureUnit());
		ingredient.setName(new IngredientName());
		model.addAttribute("ingredient", ingredient);
		model.addAttribute("recipeId", recipeId);
		model.addAttribute("measureUnitList", measureUnitService.findAllMeasureUnits());
		model.addAttribute("ingredientNameList", ingredientNameService.findAllIngredientNames());
		
		return "ingredient/create";
	}
	
	@PostMapping("create/{recipeId}")
	public String saveIngredient(@PathVariable String recipeId, @ModelAttribute Ingredient ingredient, Model model) {

		ingredient.setMeasureUnit(measureUnitService.findMeasureUnitById(ingredient.getMeasureUnit().getId()));
		ingredient.setName(ingredientNameService.findIngredientNameById(ingredient.getName().getId()));
		Ingredient savedIngredient = ingredientService.createIngredientForRecipe(ingredient, Long.valueOf(recipeId));
		model.addAttribute("ingredientList", recipeService.findRecipeById(Long.valueOf(recipeId)).getIngredients());
		model.addAttribute("recipeId", recipeId);
		return "redirect:/ingredient/list/" + recipeId;
	}	
	
	@GetMapping("/{ingredientId}/update/{recipeId}")
	public String updateIngredient(@PathVariable String recipeId, @PathVariable String ingredientId, Model model) {
		Ingredient ingredient = ingredientService.findIngredientById(Long.valueOf(ingredientId));
		
		model.addAttribute("ingredient", ingredient);
		model.addAttribute("recipeId", recipeId);
		model.addAttribute("measureUnitList", measureUnitService.findAllMeasureUnits());
		model.addAttribute("ingredientNameList", ingredientNameService.findAllIngredientNames());
		return "ingredient/update";
	}
	
	@PostMapping("/update/{recipeId}")
	public String updateIngredient(@PathVariable String recipeId, @ModelAttribute Ingredient ingredient, Model model) {
		ingredient.setMeasureUnit(measureUnitService.findMeasureUnitById(ingredient.getMeasureUnit().getId()));
		ingredient.setName(ingredientNameService.findIngredientNameById(ingredient.getName().getId()));
		ingredientService.updateIngredient(ingredient);
		model.addAttribute("ingredientList", recipeService.findRecipeById(Long.valueOf(recipeId)).getIngredients());
		model.addAttribute("recipeId", recipeId);
		return "redirect:/ingredient/list/" + recipeId;
	}
	
	// TODO make this a delete mapping
	@GetMapping("{ingredientId}/delete/{recipeId}")
	public String deleteIngredient(@PathVariable String ingredientId, @PathVariable String recipeId, Model model) {
		ingredientService.deleteIngredient(Long.valueOf(ingredientId));
		model.addAttribute("ingredientList", recipeService.findRecipeById(Long.valueOf(recipeId)).getIngredients());
		model.addAttribute("recipeId", recipeId);
		return "redirect:/ingredient/list/" + recipeId;
	}
}

