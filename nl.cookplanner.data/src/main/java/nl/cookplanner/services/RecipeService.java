package nl.cookplanner.services;

import java.util.Set;

import nl.cookplanner.model.Recipe;

public interface RecipeService {

	public Set<Recipe> findAllRecipes();
	
	public Recipe findRecipeById(Long id);
	
	public void deleteRecipeById(Long id);
	
	public Recipe updateRecipe(Recipe recipe);
	
	public Recipe createRecipe(Recipe recipe);
}
