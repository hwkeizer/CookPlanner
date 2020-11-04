package nl.cookplanner.services;

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

@Service
@Slf4j
public class PlanBoardService {

	private final PlanningRepository planningRepository;
	
	public PlanBoardService(PlanningRepository planningRepository) {
		this.planningRepository = planningRepository;
	}
	
	public Optional<Planning> findById(Long planningId) {
		return planningRepository.findById(planningId);
	}

	/**
	 * Returns the current planningList ordered by date
	 */
	public List<Planning> getPlanningList() {
		List<Planning> planningList = planningRepository.findAll();
		Collections.sort(planningList);
		return planningList;
	}

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
	
	public boolean addPlanning(Recipe recipe) {
		Planning planning = new Planning(getFirstAvailableDate(), recipe, true);
		planningRepository.save(planning);
		return true;
	}
	
	public boolean addPlanning() {
		Planning planning = new Planning(getFirstAvailableDate());
		planningRepository.save(planning);
		return true;
	}
	
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
	public Planning setOnShoppingList(Long planningId, boolean onShoppingList) {
		log.debug("PlanningID: {} {}", planningId, onShoppingList);
		Optional<Planning> optionalPlanning = planningRepository.findById(planningId);
		if (optionalPlanning.isPresent()) {
			optionalPlanning.get().setOnShoppingList(onShoppingList);
		}
		return planningRepository.save(optionalPlanning.get());
	}

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