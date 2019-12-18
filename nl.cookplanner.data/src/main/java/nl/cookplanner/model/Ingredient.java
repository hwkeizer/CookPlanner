package nl.cookplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private Recipe recipe;
	private IngredientName name;
	private Float amount;
	private MeasureUnit measureUnit;
	
	public Ingredient(IngredientName name, Float amount, MeasureUnit measureUnit) {
		this.name = name;
		this.amount = amount;
		this.measureUnit = measureUnit;
	}	
}
