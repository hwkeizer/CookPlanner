package nl.cookplanner.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientName extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String pluralName;
	private boolean stock;
	
	public IngredientName(String name, String pluralName, boolean stock) {
		this.name = name;
		this.pluralName = pluralName;
		this.stock = stock;
	}
}
