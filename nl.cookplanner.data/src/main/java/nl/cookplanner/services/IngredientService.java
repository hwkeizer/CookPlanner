package nl.cookplanner.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.repositories.IngredientRepository;

@Service
@Slf4j
public class IngredientService {
	
	private final IngredientRepository ingredientRepository;
	private final RecipeService recipeService;

	public IngredientService(IngredientRepository ingredientRepository, RecipeService recipeService) {
		super();
		this.ingredientRepository = ingredientRepository;
		this.recipeService = recipeService;
	}

	public Set<Ingredient> findAllIngredientsForRecipe(Long recipeId) {
		Recipe recipe = recipeService.findRecipeById(recipeId);
		return recipe.getIngredients();
	}

	public Ingredient createIngredientForRecipe(Ingredient ingredient, Long recipeId) {
		ingredient.setRecipe(recipeService.findRecipeById(Long.valueOf(recipeId)));
		log.debug("New created ingredient for recipe {}: {}", recipeId, ingredient);
		ingredientRepository.save(ingredient);
		return ingredient;
	}
	
	public Ingredient createIngredientWithoutRecipe(Ingredient ingredient) {
		log.debug("New created ingredient for recipe {}: {}", ingredient);
		ingredientRepository.save(ingredient);
		return ingredient;
	}

	public void deleteIngredient(Long ingredientId) {
		Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
		if (optionalIngredient.isPresent()) {
			ingredientRepository.deleteById(ingredientId);
		}
	}

	public Ingredient findIngredientById(Long ingredientId) {
		Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
		if (optionalIngredient.isPresent()) {
			return optionalIngredient.get();
		} else {
			// TODO implement error handling
			throw new RuntimeException("Ingredient not found!");
		}
	}

	public Ingredient updateIngredient(Ingredient ingredient) {
		return ingredientRepository.save(ingredient);
	}

}
