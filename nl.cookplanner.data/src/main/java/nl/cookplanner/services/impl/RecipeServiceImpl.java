package nl.cookplanner.services.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.exception.NotFoundException;
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.services.RecipeService;

@Service
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;

	public RecipeServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}
	
	@Override
	public Set<Recipe> findAllRecipes() {
		return recipeRepository.findAll().stream().collect(Collectors.toCollection(HashSet::new));
	}
	
	@Override
	public Set<Recipe> findAllRecipesWithIngredientName(Long id) {
		return recipeRepository.findAll().stream()
				.filter(r -> r.hasIngredientWithName(id))
				.collect(Collectors.toCollection(HashSet::new));
	}



	@Override
	public Recipe findRecipeById(Long id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		if (!optionalRecipe.isPresent()) {
			throw new NotFoundException("Recept met ID " + id.toString() + " niet gevonden.");
		}
		return optionalRecipe.get();
	}

	@Override
	public void deleteRecipeById(Long id) {
		recipeRepository.deleteById(id);
	}

	// TODO consider if there should be changes between update and create
	@Override
	public Recipe updateRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}

	@Override
	public Recipe createRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}
	
	
}
