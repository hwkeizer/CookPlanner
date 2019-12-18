package nl.cookplanner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.cookplanner.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
