package nl.cookplanner.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import nl.cookplanner.exception.NotFoundException;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.repositories.TagRepository;
import nl.cookplanner.services.ImageService;
import nl.cookplanner.services.IngredientNameService;
import nl.cookplanner.services.IngredientService;
import nl.cookplanner.services.RecipeService;

public class RecipeControllerTest {

	@Mock
	RecipeService recipeService;

	@Mock
	TagRepository tagRepository;

	@Mock
	RecipeRepository recipeRepository;

	@Mock
	IngredientService ingredientService;

	@Mock
	IngredientNameService ingredientNameService;

	@Mock
	ImageService imageService;

	RecipeController recipeController;
	MockMvc mockMvc;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		recipeController = new RecipeController(recipeRepository, recipeService, tagRepository, ingredientService,
				ingredientNameService, imageService);
		mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
				.setControllerAdvice(new ControllerExceptionHandler()).build();
	}

	@Test
	public void testShowRecipeById() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(1L);

		when(recipeService.findRecipeById(anyLong())).thenReturn(recipe);

		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isOk()).andExpect(view().name("recipe/show-details"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testShowRecipeByIdNotFoundException() throws Exception {

		when(recipeService.findRecipeById(anyLong())).thenThrow(NotFoundException.class);

		mockMvc.perform(get("/recipe/1/show")).andExpect(status().isNotFound())
				.andExpect(view().name("errors/404error"));
	}

	@Test
	public void testShowRecipeByIdNumberFormatException() throws Exception {

		mockMvc.perform(get("/recipe/asdf/show")).andExpect(status().isBadRequest())
				.andExpect(view().name("errors/400error"));
	}

	@Test
	public void testGetNewRecipeForm() throws Exception {
		Recipe recipe = new Recipe();

		mockMvc.perform(get("/recipe/create")).andExpect(status().isOk()).andExpect(view().name("recipe/create"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testPostNewRecipeForm() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(2L);

		when(recipeService.createRecipe(any())).thenReturn(recipe);

		mockMvc.perform(post("/recipe/create").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", "")
				.param("name", "some name").param("recipeType", "AMUSE").param("servings", "4").param("cookTime", "20"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/recipe/2/update"));
	}

	@Test
	public void testPostNewRecipeFormValidationFail() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(2L);

		when(recipeRepository.save(any())).thenReturn(recipe);

		mockMvc.perform(post("/recipe/create").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("id", ""))
				.andExpect(status().isOk()).andExpect(model().attributeExists("recipe"))
				.andExpect(view().name("recipe/create"));
	}

	@Test
	public void testShowUpdateRecipeForm() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(2L);
		Optional<Recipe> optionalRecipe = Optional.of(recipe);

		when(recipeRepository.findById(anyLong())).thenReturn(optionalRecipe);

		mockMvc.perform(get("/recipe/1/update")).andExpect(status().isOk()).andExpect(view().name("recipe/update"))
				.andExpect(model().attributeExists("recipe"));
	}

	@Test
	public void testDeleteRecipe() throws Exception {
		mockMvc.perform(get("/recipe/1/delete")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/"));

		verify(recipeService, times(1)).deleteRecipeById(anyLong());
	}

}
