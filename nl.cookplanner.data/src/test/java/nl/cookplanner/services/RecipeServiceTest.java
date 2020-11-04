package nl.cookplanner.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import nl.cookplanner.model.Recipe;
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.services.RecipeService;
import nl.cookplanner.utilities.TestData;


class RecipeServiceTest {

	@Mock
	RecipeRepository recipeRepository;

	RecipeService recipeService;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		recipeService = new RecipeService(recipeRepository);
	}

	@Test
	void testFindAllRecipes() {

		// Given
		List<Recipe> recipes = TestData.getRecipeListWithIngredients(5);

		// When
		when(recipeRepository.findAll()).thenReturn(recipes);

		// Then
		assertIterableEquals(new HashSet<>(recipes), recipeService.findAllRecipes(),
				"The returned recipeSet should be correctly returned");
	}

	@Test
	void testFindAllRecipesWithIngredientName() {
		
		// Given
		List<Recipe> recipes = TestData.getRecipeListWithIngredients(5);
		recipes.get(0).getIngredients().add(TestData.createIngredient(1000, TestData.createIngredientName(11), 2f, TestData.createMeasureUnit(11)));
		recipes.get(4).getIngredients().add(TestData.createIngredient(1001, TestData.createIngredientName(11), 3f, TestData.createMeasureUnit(11)));
		List<Recipe> expectedRecipes = new ArrayList<Recipe>(recipes);
		expectedRecipes.remove(3);
		expectedRecipes.remove(2);
		
		// When
		when(recipeRepository.findAll()).thenReturn(recipes);
		
		// Then
		assertIterableEquals(new HashSet<>(expectedRecipes), recipeService.findAllRecipesWithIngredientName(TestData.createIngredientName(11).getId()),
				"The returned recipeSet should contain the three recipes that contain 'ingredientName11'");
	}

}
