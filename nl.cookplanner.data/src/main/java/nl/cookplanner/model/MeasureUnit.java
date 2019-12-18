package nl.cookplanner.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeasureUnit extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String pluralName;
	
	public MeasureUnit(String name, String pluralName) {
		this.name = name;
		this.pluralName = pluralName;
	}
}
