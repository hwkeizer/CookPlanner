package nl.cookplanner.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.UpdatePlanDates;
import nl.cookplanner.repositories.PlanningRepository;
import nl.cookplanner.services.PlanBoardService;

@Service
@Slf4j
public class PlanBoardServiceImpl implements PlanBoardService {

	private final PlanningRepository planningRepository;
	
	public PlanBoardServiceImpl(PlanningRepository planningRepository) {
		this.planningRepository = planningRepository;
	}
	
	@Override
	public Optional<Planning> findById(Long planningId) {
		return planningRepository.findById(planningId);
	}

	/**
	 * Returns the current plannings ordered by date
	 */
	@Override
	public List<Planning> getPlannings() {
		List<Planning> planningList = planningRepository.findAll();
		Collections.sort(planningList);
		return planningList;
	}

	@Override
	public void deletePlanningById(Long planningId) {
		Optional<Planning> optionalPlanning = planningRepository.findById(planningId);
		if (optionalPlanning.isPresent()) {
			Planning planning = optionalPlanning.get();
			if (planning.getDate().isBefore(LocalDate.now()) || planning.getDate().equals(LocalDate.now())) {
				for (Recipe recipe : planning.getRecipes()) {
					recipe.setLastServed(planning.getDate());
					recipe.setTimesServed(recipe.getTimesServed() != null ? recipe.getTimesServed()+ 1 : 1);
				}
			}
			
		}
		planningRepository.deleteById(planningId);
		
	}
	
	@Override
	public boolean addPlanning(Recipe recipe) {
		Planning planning = new Planning(getFirstAvailableDate(), recipe, true);
		planningRepository.save(planning);
		return true;
	}
	
	@Override
	public boolean addPlanning() {
		Planning planning = new Planning(getFirstAvailableDate());
		planningRepository.save(planning);
		return true;
	}
	
	@Override
	public void updatePlanning(Planning planning) {
		planningRepository.save(planning);
	}
	
	/**
	 * Return The first available date that is not in the past and has not already a planning
	 */
	private LocalDate getFirstAvailableDate() {
		LocalDate localDate = LocalDate.now();
		List<Planning> planningList = planningRepository.findAll();
		if (planningList.isEmpty()) return localDate;
		Collections.sort(planningList);
		for (Planning planning : planningList) {
			if (planning.getDate().isBefore(localDate)) continue;	
			if (!planning.getDate().equals(localDate)) {
				return localDate;
			} else localDate = localDate.plusDays(1);
		}
		return localDate;
	}

	/**
	 * Turn onShoppingList on or off for this planning
	 */
	@Override
	public Planning setOnShoppingList(Long planningId, boolean onShoppingList) {
		log.debug("PlanningID: {} {}", planningId, onShoppingList);
		Optional<Planning> optionalPlanning = planningRepository.findById(planningId);
		if (optionalPlanning.isPresent()) {
			optionalPlanning.get().setOnShoppingList(onShoppingList);
		}
		return planningRepository.save(optionalPlanning.get());
	}

	// TODO: Test this method thoroughly and check if this code can be improved
	@Override
	public List<Ingredient> getShoppingList(boolean stock) {
		List<Ingredient> ingredients = getShoppingListIngredients(stock);
		List<Ingredient> result = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			boolean exists = false; 
			for (Ingredient resultIngredient : result) {
				if (ingredient.getName().equals(resultIngredient.getName()) && ingredient.getMeasureUnit().equals(resultIngredient.getMeasureUnit())) {
					exists = true;
					if (ingredient.getAmount() != null && resultIngredient.getAmount() != null) {
						resultIngredient.setAmount(ingredient.getAmount() + resultIngredient.getAmount());
					}
				}
			}
			if (!exists) {
				result.add(new Ingredient(ingredient.getName(),
						ingredient.getAmount(),
						ingredient.getMeasureUnit()));
			}
		}
		return result;
	}
	
	
	public List<Ingredient> getShoppingListIngredients(boolean isStock) {
		List<Ingredient> ingredients = new ArrayList<>();
		List<Planning> planningList = planningRepository.findAll();
		for (Planning planning : planningList) {
			if (planning.isOnShoppingList()) {
				for (Recipe recipe : planning.getRecipes()) {
					for (Ingredient ingredient : recipe.getIngredients()) {
						if (ingredient.getName().isStock() == isStock) {
							ingredients.add(ingredient);
						}
					}
					
				}
			}
		}
		return ingredients;
	}

	@Override
	public void updateNewPlanDates(List<UpdatePlanDates> newPlanDates) {
		List<Planning> currentPlanningList = planningRepository.findAll();
		for (Planning planning : currentPlanningList) {
			for (UpdatePlanDates updatedPlanDate : newPlanDates) {
				if (planning.getId() == updatedPlanDate.getId()) {
					planning.setDate(updatedPlanDate.getDate());
				}
			}
		}
		planningRepository.saveAll(currentPlanningList);
	}
}