package nl.cookplanner.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.cookplanner.model.IngredientName;

public interface IngredientNameRepository extends JpaRepository<IngredientName, Long>{

	Optional<IngredientName> findByName(String string);

}
