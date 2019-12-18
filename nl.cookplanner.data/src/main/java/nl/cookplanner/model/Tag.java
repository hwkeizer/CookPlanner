package nl.cookplanner.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Tag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NonNull
	@Column(unique = true, length = 30)
	private String name;

//	@ManyToMany(mappedBy = "tags")
//	private Set<Recipe> recipes = new HashSet<>();

	public Tag(String name) {
		this.name = name;
	}
}
