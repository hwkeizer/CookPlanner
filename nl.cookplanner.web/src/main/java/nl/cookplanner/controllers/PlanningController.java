package nl.cookplanner.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.UpdatePlanDates;
import nl.cookplanner.services.PlanBoardService;
import nl.cookplanner.services.RecipeService;

@Controller
@Slf4j
public class PlanningController extends AbstractController {
	
	private final RecipeService recipeService;
	private final PlanBoardService planBoardService;
	
	public PlanningController(RecipeService recipeService, PlanBoardService planBoardService) {
		this.recipeService = recipeService;
		this.planBoardService = planBoardService;
	}
	
	/**
	 * Show the planning overview
	 */
	@GetMapping("planning/overview")
	public String showPlanningOverview(Model model) {
		List<Planning> planningList = planBoardService.getPlannings();
		model.addAttribute("plannings", planningList);
		return "planning/overview";
	}

	/*
	 * Create a new planning with an initial recipeId
	 */
	@PostMapping("planning/{recipeId}/new")
	public String addNewPlanning(@PathVariable String recipeId) {
		Recipe recipe = recipeService.findRecipeById(Long.valueOf(recipeId));
		planBoardService.addPlanning(recipe);
		return "redirect:/recipe/list";
	}
	
	/**
	 * Create a new empty planning
	 */
	@PostMapping("planning/new")
	public String addEmptyPlanning() {
		planBoardService.addPlanning();
		return "redirect:/planning/overview";
	}
	
	@GetMapping("/planning/{id}/update")
	public String getPlanningUpdateForm(Model model, @PathVariable String id) {
		Optional<Planning> planning = planBoardService.findById(Long.valueOf(id));
		if (planning.isPresent()) {
			model.addAttribute("planning", planning.get());
			model.addAttribute("recipeList", recipeService.findAllRecipes());
			model.addAttribute("newRecipeId");
		} 
		return "planning/update";
	}
	
	@PostMapping("/planning/update")
	public String updatePlanning(@Valid @ModelAttribute("planning") Planning planning, 
			@ModelAttribute("newRecipeId") String id, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {log.debug(objectError.toString());});
		}
		if (!id.isEmpty()) {
			Recipe recipe = recipeService.findRecipeById(Long.valueOf(id));
			planning.addRecipe(recipe);
		}
		planBoardService.updatePlanning(planning);
		return "redirect:/planning/overview";
	}
	
	/**
	 * Delete a planning from the list
	 */
	// TODO: Should be done with DELETE request
	@GetMapping("/planning/{planningId}/delete")
	public String deletePlanning(@PathVariable String planningId) {
		planBoardService.deletePlanningById(Long.valueOf(planningId));
		return "redirect:/planning/overview";
	}
	
	/**
	 * Process the new plan dates after a row reordering event
	 */
	@PostMapping("planning/newdates")
	public String updatePlanningWithNewDates(@RequestBody  String planDates, Model model) {
		planBoardService.updateNewPlanDates(processUpdatePlandates(planDates));
		List<Planning> planningList = planBoardService.getPlannings();
		model.addAttribute("plannings", planningList);
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
	
	@GetMapping("/planning/shopping")
	public String generateShoppingList(Model model) {
		List<Ingredient> shoppingList = planBoardService.getShoppingList(false);
		log.debug("Boodschappenlijst: {}", shoppingList);
		List<Ingredient> stockList = planBoardService.getShoppingList(true);
		model.addAttribute("shoppingList", shoppingList);
		model.addAttribute("stockList", stockList);
		return "planning/shoppinglist";
	}
	
	List<UpdatePlanDates> processUpdatePlandates(String planDates) {
		ObjectMapper mapper  = new ObjectMapper()
		        .findAndRegisterModules()
		        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		List<UpdatePlanDates> updatePlanDates = null;
		try {
			updatePlanDates = mapper.readValue(planDates,new TypeReference<List<UpdatePlanDates>>() {});
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return updatePlanDates;
	}
}