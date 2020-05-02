package nl.cookplanner.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import nl.cookplanner.utilities.TestData;

class PlanningTest {

	@Test
	void testGetRecipesOrderedByType() {
		Planning planning = new Planning();
		planning.setRecipes(TestData.getUnorderedRecipeList());

		assertIterableEquals(planning.getRecipesOrderedByType(), TestData.getOrderedRecipeList(),
				"Recipes should be in correct order");
	}

}
