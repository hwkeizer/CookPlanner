package nl.cookplanner.services;

import java.util.Set;

import nl.cookplanner.model.Ingredient;

public interface IngredientService {

	public Set<Ingredient> findAllIngredientsForRecipe(Long recipeId);
	
	public Ingredient createIngredientForRecipe(Ingredient ingredient, Long recipeId);
	
	public void deleteIngredient(Long ingredientId);
	
	public Ingredient updateIngredient(Ingredient ingredient);
	
	public Ingredient findIngredientById(Long ingredientId);
	
}
