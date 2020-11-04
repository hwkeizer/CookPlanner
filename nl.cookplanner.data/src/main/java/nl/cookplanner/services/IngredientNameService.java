package nl.cookplanner.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import nl.cookplanner.model.IngredientName;
import nl.cookplanner.repositories.IngredientNameRepository;

@Service
public class IngredientNameService {

	private final IngredientNameRepository ingredientNameRepository;

	public IngredientNameService(IngredientNameRepository ingredientNameRepository) {
		this.ingredientNameRepository = ingredientNameRepository;
	}

	public Set<IngredientName> findAllIngredientNames() {
		
		Set<IngredientName> ingredientNameSet = new HashSet<>();
		ingredientNameRepository.findAll().iterator().forEachRemaining(ingredientNameSet::add);
		return ingredientNameSet;
	}

	public IngredientName findIngredientNameById(Long id) {
		Optional<IngredientName> optionalIngredientName = ingredientNameRepository.findById(id);
		if (optionalIngredientName.isPresent()) {
			return optionalIngredientName.get();
		} else {
			// TODO implement errorhandling
		}
		return null;
	}
	
}
