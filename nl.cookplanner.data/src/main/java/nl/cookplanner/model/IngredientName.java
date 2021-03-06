package nl.cookplanner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class IngredientName {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique=true, length = 50)
	private String name;
	
	@Column(length = 50)
	private String pluralName;
	
	private boolean stock = false;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ShopType shopType;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private IngredientType ingredientType;
	
	public IngredientName(String name, String pluralName) {
		this(name, pluralName, false);
	}
	
	public IngredientName(String name, String pluralName, boolean stock) {
		this.name = name;
		this.pluralName = pluralName;
		this.stock = stock;
		this.shopType = ShopType.OVERIG;
		this.ingredientType = IngredientType.OVERIG;
	}
}