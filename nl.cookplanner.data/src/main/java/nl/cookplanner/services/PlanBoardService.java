package nl.cookplanner.services;

import java.util.List;
import java.util.Optional;

import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.UpdatePlanDates;

public interface PlanBoardService {
	
	boolean addPlanning(Recipe recipe);
	
	boolean addPlanning();
	
	void updatePlanning(Planning planning);
	
	Planning setOnShoppingList(Long planningId, boolean onShoppingList);
	
	void deletePlanningById(Long planningId);
	
	Optional<Planning> findById(Long planningId);
	
	List<Planning> getPlannings();
	
	List<Ingredient> getShoppingList(boolean stock);
	
	List<Ingredient> getShoppingListIngredients(boolean stock);

	void updateNewPlanDates(List<UpdatePlanDates> newPlanDates);
}