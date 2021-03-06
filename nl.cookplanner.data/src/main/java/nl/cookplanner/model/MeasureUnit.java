package nl.cookplanner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class MeasureUnit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique=true)
	private String name;
	
	private String pluralName;
	
	public MeasureUnit(String name, String pluralName) {
		this.name = name;
		this.pluralName = pluralName;
	}
	
	public String getDisplayName() {
		if (name.equals("@")) {
			return " ";
		} else {
			return name;
		}
	}
	
	public String getPluralDisplayName() {
		if (name.equals("@")) {
			return " ";
		} else {
			return pluralName;
		}
	}
}
