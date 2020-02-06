package nl.cookplanner.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import nl.cookplanner.model.IngredientName;
import nl.cookplanner.repositories.IngredientNameRepository;
import nl.cookplanner.services.IngredientNameService;

@Service
public class IngredientNameServiceImpl implements IngredientNameService{

	private final IngredientNameRepository ingredientNameRepository;

	public IngredientNameServiceImpl(IngredientNameRepository ingredientNameRepository) {
		this.ingredientNameRepository = ingredientNameRepository;
	}

	@Override
	public Set<IngredientName> findAllIngredientNames() {
		
		Set<IngredientName> ingredientNameSet = new HashSet<>();
		ingredientNameRepository.findAll().iterator().forEachRemaining(ingredientNameSet::add);
		return ingredientNameSet;
	}
	
}
