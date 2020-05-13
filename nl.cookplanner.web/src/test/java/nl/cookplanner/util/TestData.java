package nl.cookplanner.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.RecipeType;

public class TestData {

	public static Recipe createRecipeWithIngredient() {
		Recipe recipe = getRecipe(1L, "Created recipe", RecipeType.HOOFDGERECHT);

		Ingredient ingredient = new Ingredient();
		ingredient.setId(1L);
		recipe.addIngredient(ingredient);

		return recipe;
	}
	
	public static Set<Recipe> getUnorderedRecipeSet() {
		Set<Recipe> recipeList = new HashSet<>();
		recipeList.add(getRecipe(1L, "Hoofdgerecht", RecipeType.HOOFDGERECHT));
		recipeList.add(getRecipe(2L, "Nagerecht", RecipeType.NAGERECHT));
		recipeList.add(getRecipe(3L, "Amuse", RecipeType.AMUSE));
		recipeList.add(getRecipe(4L, "Hoofdgerecht", RecipeType.HOOFDGERECHT));
		recipeList.add(getRecipe(5L, "Voorgerecht", RecipeType.VOORGERECHT));
		return recipeList;
	}

	public static Ingredient createIngredient() {

		Ingredient ingredient = new Ingredient();
		ingredient.setId(1L);
		MeasureUnit measureUnit = new MeasureUnit("testName", "testnames");
		measureUnit.setId(1L);
		IngredientName ingredientName = new IngredientName("testName", "testNames");
		ingredientName.setId(1L);
		ingredient.setMeasureUnit(measureUnit);
		ingredient.setName(ingredientName);

		return ingredient;
	}
	
	public static List<Planning> getPlanningList() {
		List<Planning> planningList = new ArrayList<>();
		planningList.add(getPlanning(1L, 0, getRecipe(1L, "first recipe", RecipeType.HOOFDGERECHT), true));
		planningList.add(getPlanning(2L, 1, getRecipe(2L, "second recipe", RecipeType.HOOFDGERECHT), true));
		planningList.add(getPlanning(3L, 2, getRecipe(3L, "third recipe", RecipeType.NAGERECHT), true));
		return planningList;
	}
	
	public static String getNewDatesForPlanningList() {
		return "[{\"id\" : 1, \"date\" : \"" + LocalDate.now().plusDays(2) + "\"},{\"id\" : 2, \"date\" : \""
				+ LocalDate.now().plusDays(1) + "\"},{\"id\" : 3, \"date\" : \"" + LocalDate.now().plusDays(0) + "\"}]";
	}
	
	public static Planning getPlanning(Long id, int diffDays, Recipe recipe, boolean onShoppingList) {
		Planning planning = new Planning(LocalDate.now().plusDays(diffDays), recipe, onShoppingList);
		planning.setId(id);
		return planning;
	}

	public static Recipe getRecipe(Long id, String name, RecipeType recipeType) {
		Recipe recipe = new Recipe(name);
		recipe.setId(id);
		recipe.setRecipeType(recipeType);
		recipe.setServings(2);
		return recipe;
	}
}
