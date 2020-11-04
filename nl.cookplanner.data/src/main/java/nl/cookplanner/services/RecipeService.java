package nl.cookplanner.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.exception.NotFoundException;
import nl.cookplanner.repositories.RecipeRepository;

@Service
public class RecipeService {

	private final RecipeRepository recipeRepository;

	public RecipeService(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}
	
	public Set<Recipe> findAllRecipes() {
		return recipeRepository.findAll().stream().collect(Collectors.toCollection(HashSet::new));
	}
	
	public Set<Recipe> findAllRecipesWithIngredientName(Long id) {
		return recipeRepository.findAll().stream()
				.filter(r -> r.hasIngredientWithName(id))
				.collect(Collectors.toCollection(HashSet::new));
	}

	public Recipe findRecipeById(Long id) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		if (!optionalRecipe.isPresent()) {
			throw new NotFoundException("Recept met ID " + id.toString() + " niet gevonden.");
		}
		return optionalRecipe.get();
	}

	public void deleteRecipeById(Long id) {
		recipeRepository.deleteById(id);
	}

	// TODO consider if there should be changes between update and create
	public Recipe updateRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}

	public Recipe createRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}
	
	
}
