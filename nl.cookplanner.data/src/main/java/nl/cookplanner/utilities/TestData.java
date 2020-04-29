package nl.cookplanner.utilities;

import java.util.ArrayList;
import java.util.List;

import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.RecipeType;

public class TestData {
	
	public static List<Recipe> getUnorderedRecipeList() {
		List<Recipe> recipeList = new ArrayList<>();
		recipeList.add(getRecipe(1L, "Hoofdgerecht", RecipeType.HOOFDGERECHT));
		recipeList.add(getRecipe(2L, "Nagerecht", RecipeType.NAGERECHT));
		recipeList.add(getRecipe(3L, "Amuse", RecipeType.AMUSE));
		recipeList.add(getRecipe(4L, "Hoofdgerecht", RecipeType.HOOFDGERECHT));
		recipeList.add(getRecipe(5L, "Voorgerecht", RecipeType.VOORGERECHT));
		return recipeList;
	}
	
	public static List<Recipe> getOrderedRecipeList() {
		List<Recipe> recipeList = new ArrayList<>();
		recipeList.add(getRecipe(3L, "Amuse", RecipeType.AMUSE));
		recipeList.add(getRecipe(5L, "Voorgerecht", RecipeType.VOORGERECHT));
		recipeList.add(getRecipe(1L, "Hoofdgerecht", RecipeType.HOOFDGERECHT));
		recipeList.add(getRecipe(4L, "Hoofdgerecht", RecipeType.HOOFDGERECHT));
		recipeList.add(getRecipe(2L, "Nagerecht", RecipeType.NAGERECHT));
		return recipeList;
	}
	
	private static Recipe getRecipe(Long id, String name, RecipeType recipeType) {
		Recipe recipe = new Recipe(name);
		recipe.setId(id);
		recipe.setRecipeType(recipeType);
		return recipe;
	}
}
