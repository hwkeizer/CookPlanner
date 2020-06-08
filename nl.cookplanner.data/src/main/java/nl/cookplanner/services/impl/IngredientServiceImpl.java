package nl.cookplanner.services.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.repositories.IngredientRepository;
import nl.cookplanner.services.IngredientService;
import nl.cookplanner.services.RecipeService;

@Service
@Slf4j
public class IngredientServiceImpl implements IngredientService {
	
	private final IngredientRepository ingredientRepository;
	private final RecipeService recipeService;

	public IngredientServiceImpl(IngredientRepository ingredientRepository, RecipeService recipeService) {
		super();
		this.ingredientRepository = ingredientRepository;
		this.recipeService = recipeService;
	}

	@Override
	public Set<Ingredient> findAllIngredientsForRecipe(Long recipeId) {
		Recipe recipe = recipeService.findRecipeById(recipeId);
		log.debug("Ingredientlist for recipe {}:  {}", recipeId, recipe.getIngredients());
		return recipe.getIngredients();
	}

	@Override
	public Ingredient createIngredientForRecipe(Ingredient ingredient, Long recipeId) {
		ingredient.setRecipe(recipeService.findRecipeById(Long.valueOf(recipeId)));
		log.debug("New created ingredient for recipe {}: {}", recipeId, ingredient);
		ingredientRepository.save(ingredient);
		return ingredient;
	}

	@Override
	public void deleteIngredient(Long ingredientId) {
		Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
		if (optionalIngredient.isPresent()) {
			ingredientRepository.deleteById(ingredientId);
		}
	}

	@Override
	public Ingredient findIngredientById(Long ingredientId) {
		Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
		if (optionalIngredient.isPresent()) {
			return optionalIngredient.get();
		} else {
			// TODO implement error handling
			throw new RuntimeException("Ingredient not found!");
		}
	}

	@Override
	public Ingredient updateIngredient(Ingredient ingredient) {
		return ingredientRepository.save(ingredient);
	}

}
