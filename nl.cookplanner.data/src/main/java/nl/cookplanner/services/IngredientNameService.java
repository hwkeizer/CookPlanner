package nl.cookplanner.services;

import java.util.Set;

import nl.cookplanner.model.IngredientName;

public interface IngredientNameService {

	Set<IngredientName> findAllIngredientNames();
	
	public IngredientName findIngredientNameById(Long id);
}
