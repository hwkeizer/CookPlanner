package nl.cookplanner.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tag extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private String name;

	public Tag(String name) {
		this.name = name;
	}
}
