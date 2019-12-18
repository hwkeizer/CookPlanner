package nl.cookplanner.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Recipe extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;
	private String servingTips;
	private String notes;
	private Set<Ingredient> ingredients = new HashSet<>();
	private String image;
	private RecipeType recipeType;
	private Set<Tag> tags = new HashSet<>();
	private Integer preparationTime;
	private Integer cookTime;
	private Integer servings;
	private String preparations;
	private String directions;
	private Integer rating;
	
	public Recipe(String name) {
		this.name = name;
	}
	
	public void setIngredients(Set<Ingredient> ingredients) {
		this.ingredients.clear();
		for (Ingredient ingredient : ingredients) {
			addIngredient(ingredient);
		}
	}
	
	public void addIngredient(Ingredient ingredient) {
		ingredient.setRecipe(this);
		ingredients.add(ingredient);
	}
	
	public void removeIngredient(Ingredient ingredient) {
		ingredients.remove(ingredient);
		
	}
}
