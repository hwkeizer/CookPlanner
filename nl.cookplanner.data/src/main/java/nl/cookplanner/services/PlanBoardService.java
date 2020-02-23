package nl.cookplanner.services;

import java.time.LocalDate;
import java.util.List;

import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;

public interface PlanBoardService {

	boolean savePlanBoard();
	
	boolean addPlanning(Recipe recipe);
	
	boolean addPlanning();
	
	void setOnShoppingList(Long planningId, boolean onShoppingList);
	
	public boolean removePlanning(Long planningId);
	
	public void movePlanning(Long planningId, LocalDate localDate);
	
	void deletePlanningById(Long planningId);
	
	Iterable<Planning> getPlannings();
	
	List<Ingredient> getShoppingList(boolean stock);
}