package nl.cookplanner.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.services.PlanBoardService;
import nl.cookplanner.services.RecipeService;

@Controller
@Slf4j
public class PlanningController extends AbstractController {
	
	private final RecipeService recipeService;
	private final PlanBoardService planBoardService;
	
	public PlanningController(RecipeService recipeService, PlanBoardService planningService) {
		this.recipeService = recipeService;
		this.planBoardService = planningService;
	}
	
	@GetMapping("planning/overview")
	public String showPlanningOverview(Model model) {
		model.addAttribute("plannings", planBoardService.getPlannings());
		return "/planning/overview";
	}
	
	// TODO: Should this be a postmapping?
	@GetMapping("planning/update")
	public String updatePlanning() {
		planBoardService.savePlanBoard();
		return "redirect:/planning/overview";
	}

	@PostMapping("planning/{recipeId}/new")
	public String addNewPlanning(@PathVariable String recipeId) {
		Recipe recipe = recipeService.findRecipeById(Long.valueOf(recipeId));
		planBoardService.addPlanning(recipe);
		return "redirect:/recipe/list";
	}
	
	@PostMapping("planning/new")
	public String addEmptyPlanning() {
		planBoardService.addPlanning();
		return "redirect:/planning/overview";
	}
	
	// TODO: Check if this should be done with Get...
	@GetMapping("/planning/{planningId}/delete")
	public String deletePlanning(@PathVariable String planningId) {
		planBoardService.removePlanning(Long.valueOf(planningId));
		return "redirect:/planning/overview";
	}
	
	@GetMapping("/planning/{planningId}/shopping_off")
	public String shoppingOff(@PathVariable String planningId) {
		planBoardService.setOnShoppingList(Long.valueOf(planningId), false);
		return "redirect:/planning/overview";
	}
	
	@GetMapping("/planning/{planningId}/shopping_on")
	public String shoppingOn(@PathVariable String planningId) {
		planBoardService.setOnShoppingList(Long.valueOf(planningId), true);
		return "redirect:/planning/overview";
	}
	
	// TODO: Test
	@PostMapping("/planning/{id}/{date}/update")
	public String updatePlanning(@PathVariable String id, @PathVariable String date) {
		planBoardService.movePlanning(Long.valueOf(id), LocalDate.parse(date));
		return "redirect:/planning/overview";
	}
	
	@GetMapping("/planning/shopping")
	public String generateShoppingList(Model model) {
		List<Ingredient> shoppingList = planBoardService.getShoppingList(false);
		List<Ingredient> stockList = planBoardService.getShoppingList(true);
		model.addAttribute("shoppingList", shoppingList);
		model.addAttribute("stockList", stockList);
		return "planning/shoppinglist";
	}
}