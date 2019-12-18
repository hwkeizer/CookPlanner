package nl.cookplanner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.cookplanner.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
