package nl.cookplanner.services;

import org.springframework.stereotype.Service;

import nl.cookplanner.model.Recipe;

@Service
public class ImageService {
	
	private final RecipeService recipeService;

	public ImageService(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	public String getCurrentRecipeImage(Long recipeId) {
		Recipe recipe = recipeService.findRecipeById(recipeId);
		return recipe.getImage();
	}

}
