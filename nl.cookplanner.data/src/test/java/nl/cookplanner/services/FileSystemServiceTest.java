package nl.cookplanner.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.RecipeType;
import nl.cookplanner.services.FileSystemService;
import nl.cookplanner.services.RecipeService;
import nl.cookplanner.utilities.TestData;

@Slf4j
class FileSystemServiceTest {

	private static final String IMAGE_PATH = "/home/henk/Data/CookPlanner/Images/InUse/";
	private static final String RECIPE_NAME = "Test recipe name";
	private static final String PNG_EXT = ".png";
	private static final String DOC_EXT = ".doc";
	
	@Mock
	RecipeService  recipeService;
	
	FileSystemService fileSystemService;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		fileSystemService = new FileSystemService(recipeService);
		ReflectionTestUtils.setField(fileSystemService, "imageFolder", IMAGE_PATH);
	}
	
	@Test
	void testUploadImage() {

		MockMultipartFile image = new MockMultipartFile("data", "random.png", MediaType.IMAGE_PNG_VALUE, "some data".getBytes());
		Recipe recipe = TestData.getRecipe(2L, RECIPE_NAME, RecipeType.TUSSENGERECHT);
		
		Path testFilePath = removeTestImageIfExists(RECIPE_NAME);
		assertEquals(false, testFilePath.toFile().exists());

		when(recipeService.findRecipeById(recipe.getId())).thenReturn(recipe);
		fileSystemService.uploadImageForRecipe(image, recipe.getId());
		assertEquals(RECIPE_NAME + PNG_EXT, recipe.getImage());
		assertEquals(true, testFilePath.toFile().exists());
		
		removeTestImageIfExists(RECIPE_NAME);
	}

	@Test
	void testGetFileNameExtension() {
		assertEquals(".drew", fileSystemService.getFileNameExtension("Kral.freq.drew"));
		assertEquals(".drew", fileSystemService.getFileNameExtension("$.drew"));
		assertEquals("", fileSystemService.getFileNameExtension("file/without/extension"));
		assertEquals("", fileSystemService.getFileNameExtension(null));
	}
	
	@Test
	void testCreatePathForRecipeImage() {
		
		Path expectedPath = Paths.get(IMAGE_PATH, RECIPE_NAME + PNG_EXT);
		assertEquals(expectedPath, fileSystemService.createPathForRecipeImage(RECIPE_NAME, PNG_EXT));
		
		// Doc extension should not be allowed (not implemented yet)
		expectedPath = Paths.get(IMAGE_PATH, RECIPE_NAME + DOC_EXT);
		assertEquals(expectedPath, fileSystemService.createPathForRecipeImage(RECIPE_NAME, DOC_EXT));
	}
	
	private Path removeTestImageIfExists(String recipeName) {
		Path testFilePath = Path.of(IMAGE_PATH + recipeName + PNG_EXT);
		if (testFilePath.toFile().exists()) {
			testFilePath.toFile().delete();
		}
		return testFilePath;
	}
}
