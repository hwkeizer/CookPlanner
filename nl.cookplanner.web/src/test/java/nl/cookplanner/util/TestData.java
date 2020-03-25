package nl.cookplanner.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;

public class TestData {

	public static Recipe createRecipeWithIngredient() {
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		Ingredient ingredient = new Ingredient();
		ingredient.setId(1L);
		recipe.addIngredient(ingredient);

		return recipe;
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
		planningList.add(new Planning(LocalDate.now(), createRecipeWithIngredient(), true));
		return planningList;
	}
	
	public static Planning getPlanning() {
		return new Planning(LocalDate.now(), createRecipeWithIngredient(), true);
	}

}
