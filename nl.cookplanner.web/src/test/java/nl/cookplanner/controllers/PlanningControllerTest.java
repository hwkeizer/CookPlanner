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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.RecipeType;
import nl.cookplanner.model.UpdatePlanDates;
import nl.cookplanner.services.PlanBoardService;
import nl.cookplanner.services.RecipeService;
import nl.cookplanner.util.TestData;

class PlanningControllerTest {

	@Mock
	RecipeService recipeService;
	
	@Mock
	PlanBoardService planBoardService;
	
	PlanningController planningController;
	MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		planningController = new PlanningController(recipeService, planBoardService);
		mockMvc = MockMvcBuilders.standaloneSetup(planningController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.build();
	}
	
	@Test
	void testShowPlanningOverview() throws Exception {
		// Given
		List<Planning> planningList = TestData.getPlanningList();
		
		// When
		when(planBoardService.getPlannings()).thenReturn(planningList);
		
		// Then
		mockMvc.perform(get("/planning/overview"))
			.andExpect(status().isOk())
			.andExpect(view().name("planning/overview"))
			.andExpect(model().attribute("plannings", planningList));
	}

	@Test
	void testAddNewPlanning() throws Exception {
		// Given
		Recipe recipe = TestData.createRecipeWithIngredient();
		
		// When
		when(recipeService.findRecipeById(recipe.getId())).thenReturn(recipe);
		
		// Then
		mockMvc.perform(post("/planning/1/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/recipe/list"));
		
		verify(planBoardService, times(1)).addPlanning(recipe);
	}

	@Test
	void testAddEmptyPlanning() throws Exception {
		// Then
		mockMvc.perform(post("/planning/new")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/planning/overview"));
				
				verify(planBoardService, times(1)).addPlanning();
	}
	
	@Test 
	void testGetPlanningUpdateForm() throws Exception {
		// Given
		Planning planning = TestData.getPlanning(1L, 0, TestData.getRecipe(1L, "first recipe", RecipeType.HOOFDGERECHT), true);
		
		// When
		when(planBoardService.findById(planning.getId())).thenReturn(Optional.of(planning));
		when(recipeService.findAllRecipes()).thenReturn(TestData.getUnorderedRecipeSet());
		
		// Then
		mockMvc.perform(get("/planning/" + planning.getId() + "/update"))
				.andExpect(status().isOk())
				.andExpect(view().name("planning/update"))
				.andExpect(model().attribute("planning", planning))
				.andExpect(model().attribute("recipeList", TestData.getUnorderedRecipeSet()));
	}
	
	@Test
	void testUpdatePlanning() throws Exception {
		// Given
		Planning planning = TestData.getPlanning(1L, 0, TestData.getRecipe(1L, "first recipe", RecipeType.HOOFDGERECHT), true);
		Recipe recipe = TestData.getRecipe(2L, "Added recipe", RecipeType.AMUSE);
		
		// When
		when(recipeService.findRecipeById(2L)).thenReturn(recipe);
		
		// Then
		mockMvc.perform(post("/planning/update").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", planning.getId().toString())
				.param("date", planning.getDate().toString())
				.param("newRecipeId", "4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/planning/overview"));
		
		verify(planBoardService, times(1)).updatePlanning(planning);
		verify(recipeService, times(1)).findRecipeById(4L);
	}
	
	@Test
	void testDeletePlanning() throws Exception {
		// Then
		mockMvc.perform(get("/planning/1/delete"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/planning/overview"));
		verify(planBoardService, times(1)).deletePlanningById(1L);;
	}
	
	@Test
	void testProcessUpdatePlandates() throws Exception {
		// Given
		String newDates = TestData.getNewDatesForPlanningList();
		List<UpdatePlanDates> updatePlanDates = planningController.processUpdatePlandates(newDates);
		
		List<UpdatePlanDates> expectedUpdatePlandates = new ArrayList<>();
		expectedUpdatePlandates.add(new UpdatePlanDates(1L, LocalDate.now().plusDays(2)));
		expectedUpdatePlandates.add(new UpdatePlanDates(2L, LocalDate.now().plusDays(1)));
		expectedUpdatePlandates.add(new UpdatePlanDates(3L, LocalDate.now().plusDays(0)));

		assertIterableEquals(updatePlanDates, expectedUpdatePlandates, "Plandates should be in descending order");
	}

	@Test
	void testShoppingOff() throws Exception {
		// Then
		mockMvc.perform(get("/planning/1/shopping_off"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/planning/overview"));
		verify(planBoardService, times(1)).setOnShoppingList(1L, false);
	}

	@Test
	void testShoppingOn() throws Exception {
		// Then
		mockMvc.perform(get("/planning/1/shopping_on"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/planning/overview"));
		verify(planBoardService, times(1)).setOnShoppingList(1L, true);
	}
	
	@Test
	void testGenerateShoppingList() throws Exception {
		// TODO implement this
	}
}
