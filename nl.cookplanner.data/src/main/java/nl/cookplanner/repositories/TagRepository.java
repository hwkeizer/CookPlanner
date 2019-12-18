package nl.cookplanner.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.cookplanner.model.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>{

	Optional<Tag> findByName(String name);
}
