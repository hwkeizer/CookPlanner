package nl.cookplanner.services.impl;

import org.springframework.stereotype.Service;

import nl.cookplanner.model.Recipe;
import nl.cookplanner.services.ImageService;
import nl.cookplanner.services.RecipeService;

@Service
public class ImageServiceImpl implements  ImageService {
	
	private final RecipeService recipeService;

	public ImageServiceImpl(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@Override
	public String getCurrentRecipeImage(Long recipeId) {
		Recipe recipe = recipeService.findRecipeById(recipeId);
		return recipe.getImage();
	}

}
