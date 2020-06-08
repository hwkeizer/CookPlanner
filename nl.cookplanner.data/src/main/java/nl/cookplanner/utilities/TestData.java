package nl.cookplanner.utilities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.model.Planning;
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
	
	public static List<Planning> getPlanningListWithRecipesWithIngredients() {
		IngredientName potato = new IngredientName("potato", "potatoes");
		potato.setStock(false);
		IngredientName tomato = new IngredientName("tomato", "tomatoes");
		tomato.setStock(false);
		MeasureUnit gram = new MeasureUnit("gram", "gram");
		MeasureUnit kilogram = new MeasureUnit("kilogram", "kilogram");
		
		Recipe firstRecipe = getRecipe(1L, "First Recipe", RecipeType.HOOFDGERECHT);
		firstRecipe.addIngredient(getIngredient(1L, potato, gram, 300f));
		firstRecipe.addIngredient(getIngredient(2L, tomato, gram, 300f));
		
		Recipe secondRecipe = getRecipe(2L, "Second Recipe", RecipeType.VOORGERECHT);
		secondRecipe.addIngredient(getIngredient(3L, potato, gram, 300f));
		secondRecipe.addIngredient(getIngredient(4L, tomato, kilogram, 0.3f));
		
		List<Planning> planningList = new ArrayList<>();
		planningList.add(getPlanning(1L, 0, firstRecipe, true));
		planningList.add(getPlanning(2L,  1, secondRecipe, true));

		return planningList;
	}
	
	public static List<Ingredient> getExpectedShoppingList() {
		IngredientName potato = new IngredientName("potato", "potatoes");
		potato.setStock(false);
		IngredientName tomato = new IngredientName("tomato", "tomatoes");
		tomato.setStock(false);
		MeasureUnit gram = new MeasureUnit("gram", "gram");
		MeasureUnit kilogram = new MeasureUnit("kilogram", "kilogram");
		
		List<Ingredient> shoppingList = new ArrayList<>();
		shoppingList.add(getIngredient(null, tomato, gram, 300f));
		shoppingList.add(getIngredient(null, potato, gram, 600f));
		shoppingList.add(getIngredient(null, tomato, kilogram, 0.3f));
		return shoppingList;
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
	
	public static Ingredient getIngredient(Long id, IngredientName ingredientname, MeasureUnit measureUnit, Float amount) {
		Ingredient ingredient = new Ingredient();
		ingredient.setId(id);
		ingredient.setName(ingredientname);
		ingredient.setMeasureUnit(measureUnit);
		ingredient.setAmount(amount);
		return ingredient;
	}
	
	// Generate data for tests
	public static List<Recipe> getRecipeListWithIngredients(int numberOfRecipes) {
		List<Recipe> recipes = new ArrayList<>();
		for (int i = 0; i < numberOfRecipes; i++) {
			Recipe recipe = new Recipe();
			recipe.setId((long)i);
			recipe.setIngredients(getSomeIngredientsForRecipeId(i));
			recipes.add(recipe);
		}
		return recipes;
	}
	
	public static Set<Ingredient> getSomeIngredientsForRecipeId(int id) {
		Set<Ingredient> ingredients = new HashSet<>();
		int start = 10*id;
		for (int i = start; i<start + 4; i++) {
			ingredients.add(createIngredient(i));
		}
		return ingredients;
	}
	
	public static Ingredient createIngredient(int id, IngredientName ingredientName, Float amount, MeasureUnit measureUnit) {
		Ingredient ingredient = new Ingredient();
		ingredient.setId((long)id);
		ingredient.setName(ingredientName);
		ingredient.setAmount(amount);
		ingredient.setMeasureUnit(measureUnit);
		return ingredient;
	}
	
	private static Ingredient createIngredient(int id) {
		Ingredient ingredient = new Ingredient(createIngredientName(id), (float)id, createMeasureUnit(id));
		ingredient.setId((long)id);
		return ingredient;
	}
	
	public static IngredientName createIngredientName(int id) {
		IngredientName ingredientName = new IngredientName("Ingredient" + id, "Ingredients" + id);
		ingredientName.setId((long)id);
		return ingredientName;
	}
	
	public static MeasureUnit createMeasureUnit(int id) {
		MeasureUnit measureUnit = new MeasureUnit("MeasureUnit" + id, "MeasureUnits" + id);
		measureUnit.setId((long)id);
		return measureUnit;
	}
}
