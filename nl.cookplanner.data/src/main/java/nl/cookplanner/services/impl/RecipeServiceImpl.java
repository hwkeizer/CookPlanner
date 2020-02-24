package nl.cookplanner.services.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.services.RecipeService;

@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {

	private final RecipeRepository recipeRepository;

	public RecipeServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}
	
	@Override
	public Set<Recipe> findAllRecipes() {
	
		Set<Recipe> recipeSet = new HashSet<>();
		recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
		return recipeSet;
	}


	@Override
	public Recipe findRecipeById(Long id) {
		
		Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
		if (!optionalRecipe.isPresent()) {
			// TODO implement correct errorhandling
			log.error("Recept met ID " + id.toString() + " niet gevonden.");
		}
		return optionalRecipe.get();
	}

	@Override
	public void deleteRecipeById(Long id) {
		
		log.debug("delete recipe not implemented yet");
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
