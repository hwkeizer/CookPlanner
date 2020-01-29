package nl.cookplanner.model;

/** 
 * Entries in this enumeration must not be changed 
 * Entries can be reordered
 * Entries can be added
 * Entries can contain max 20 characters
 */
public enum RecipeType {
	AMUSE("Amuse"), 
	VOORGERECHT("Voorgerecht"), 
	HOOFDGERECHT("Hoofdgerecht"), 
	NAGERECHT("Nagerecht"), 
	BIJGERECHT("Bijgerecht"), 
	TUSSENGERECHT("Tussengerecht");
	
	private String displayName;
	
	private RecipeType(String displayName) {this.displayName = displayName;}
	
	public String getDisplayName() {return displayName;}
}
