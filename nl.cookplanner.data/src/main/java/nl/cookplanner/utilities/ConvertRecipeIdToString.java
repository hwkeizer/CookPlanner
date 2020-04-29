package nl.cookplanner.utilities;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import nl.cookplanner.model.Recipe;

@Component
public class ConvertRecipeIdToString implements Converter<Recipe, String>{

	@Override
	public String convert(Recipe recipe) {
		return recipe.getId().toString();
	}

}
