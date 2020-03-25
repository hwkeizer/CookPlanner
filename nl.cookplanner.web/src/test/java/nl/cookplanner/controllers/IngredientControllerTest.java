package nl.cookplanner.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.services.IngredientNameService;
import nl.cookplanner.services.IngredientService;
import nl.cookplanner.services.MeasureUnitService;
import nl.cookplanner.services.RecipeService;
import nl.cookplanner.util.TestData;

class IngredientControllerTest {

	@Mock
	IngredientService ingredientService;

	@Mock
	RecipeService recipeService;

	@Mock
	MeasureUnitService measureUnitService;

	@Mock
	IngredientNameService ingredientNameService;

	IngredientController ingredientController;
	MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ingredientController = new IngredientController(recipeService, ingredientService, measureUnitService,
				ingredientNameService);
		mockMvc = MockMvcBuilders.standaloneSetup(ingredientController)
				.setControllerAdvice(new ControllerExceptionHandler()).build();
	}

	@Test
	public void testShowIngredientListForRecipeId() throws Exception {
		// Given
		Recipe recipe = TestData.createRecipeWithIngredient();

		// When
		when(recipeService.findRecipeById(anyLong())).thenReturn(recipe);

		// Then
		mockMvc.perform(get("/ingredient/list/1")).andExpect(status().isOk()).andExpect(view().name("ingredient/list"))
				.andExpect(model().attribute("ingredientList", recipe.getIngredients()))
				.andExpect(model().attributeExists("recipeId"));

		verify(recipeService, times(1)).findRecipeById(recipe.getId());
	}

	@Test
	public void testShowCreateIngredientForm() throws Exception {
		// When
		when(measureUnitService.findAllMeasureUnits()).thenReturn(new HashSet<>());
		when(ingredientNameService.findAllIngredientNames()).thenReturn(new HashSet<>());

		// Then
		mockMvc.perform(get("/ingredient/create/1")).andExpect(status().isOk())
				.andExpect(view().name("ingredient/create")).andExpect(model().attributeExists("ingredient"))
				.andExpect(model().attributeExists("recipeId")).andExpect(model().attributeExists("measureUnitList"))
				.andExpect(model().attributeExists("ingredientNameList"));
	}

	@Test
	public void testcreateIngredient() throws Exception {
		// Given
		Ingredient ingredient = TestData.createIngredient();
		Recipe recipe = TestData.createRecipeWithIngredient();
		Set<Ingredient> ingredientList = recipe.getIngredients();
		Iterator<Ingredient> it = ingredientList.iterator();
		Ingredient createdIngredient = it.next();

		// When
		when(ingredientService.createIngredientForRecipe(ingredient, recipe.getId())).thenReturn(createdIngredient);
		when(measureUnitService.findMeasureUnitById(1L)).thenReturn(ingredient.getMeasureUnit());
		when(ingredientNameService.findIngredientNameById(1L)).thenReturn(ingredient.getName());
		when(recipeService.findRecipeById(1L)).thenReturn(recipe);

		// Then
		mockMvc.perform(post("/ingredient/create/1").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("amount", "1")
				.param("measureUnit.id", "1")
				.param("name.id", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/ingredient/list/1"))
				.andExpect(model().attribute("ingredientList", recipe.getIngredients()))
				.andExpect(model().attributeExists("recipeId"));
	}

	@Test
	public void testShowUpdateIngredientForm() throws Exception {
		// Given
		Ingredient ingredient = TestData.createIngredient();

		// When
		when(ingredientService.findIngredientById(1L)).thenReturn(ingredient);

		// Then
		mockMvc.perform(get("/ingredient/1/update/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("ingredient/update"))
				.andExpect(model().attribute("ingredient", ingredient))
				.andExpect(model().attribute("recipeId", "1"))
				.andExpect(model().attributeExists("measureUnitList"))
				.andExpect(model().attributeExists("ingredientNameList"));
	}

	@Test
	public void testUpdateIngredient() throws Exception {
		// Given
		Ingredient ingredient = TestData.createIngredient();
		Recipe recipe = TestData.createRecipeWithIngredient();
		
		// When
		when(measureUnitService.findMeasureUnitById(1L)).thenReturn(ingredient.getMeasureUnit());
		when(ingredientNameService.findIngredientNameById(1L)).thenReturn(ingredient.getName());
		when(ingredientService.updateIngredient(ingredient)).thenReturn(ingredient);
		when(recipeService.findRecipeById(1L)).thenReturn(recipe);
		
		// Then
		mockMvc.perform(post("/ingredient/update/1").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("amount", "1")
				.param("measureUnit.id", "1")
				.param("name.id", "1"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/ingredient/list/1"))
				.andExpect(model().attributeExists("ingredientList"))
				.andExpect(model().attributeExists("recipeId"));
	}

	@Test
	public void testDeleteIngredient() throws Exception {

		// Given
		Recipe recipe = TestData.createRecipeWithIngredient();

		// When
		when(recipeService.findRecipeById(2L)).thenReturn(recipe);

		mockMvc.perform(get("/ingredient/3/delete/2")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/ingredient/list/2"));

		verify(ingredientService, times(1)).deleteIngredient(3L);

	}


}
