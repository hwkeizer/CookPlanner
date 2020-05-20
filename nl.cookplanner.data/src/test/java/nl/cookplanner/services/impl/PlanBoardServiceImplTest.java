package nl.cookplanner.services.impl;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Planning;
import nl.cookplanner.repositories.PlanningRepository;
import nl.cookplanner.services.PlanBoardService;
import nl.cookplanner.utilities.TestData;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class PlanBoardServiceImplTest {
	
	@Mock
	PlanningRepository planningRepository;
	
	PlanBoardService planBoardService;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		planBoardService = new PlanBoardServiceImpl(planningRepository);
	}
	
	@Test
	void testGetShoppingList() {
		// Given
		List<Planning> list = TestData.getPlanningListWithRecipesWithIngredients();

		// When
		when(planningRepository.findAll()).thenReturn(list);

		// Then
		assertIterableEquals( TestData.getExpectedShoppingList(), planBoardService.getShoppingList(false),
				"Shoppinglist should contain merged potatoes (equal measureUnits) and seperated tomatoes (no equal measureUnits)");
	}

	@Test
	void testGetShoppingListIngredients() {
		
		// Given
		List<Planning> list = TestData.getPlanningListWithRecipesWithIngredients();
		List<Ingredient> expectedIngredientList = new ArrayList<>();
		expectedIngredientList.addAll(list.get(0).getRecipes().get(0).getIngredients());
		expectedIngredientList.addAll(list.get(1).getRecipes().get(0).getIngredients());

		// When
		when(planningRepository.findAll()).thenReturn(list);

		// Then
		assertIterableEquals(expectedIngredientList, planBoardService.getShoppingListIngredients(false),
				"Ingredientlist should contain correct amounts of tomatoes and potatoes");
	}

}
