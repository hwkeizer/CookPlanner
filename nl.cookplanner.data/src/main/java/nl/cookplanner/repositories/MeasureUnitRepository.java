package nl.cookplanner.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.cookplanner.model.MeasureUnit;

public interface MeasureUnitRepository extends JpaRepository<MeasureUnit, Long> {

	Optional<MeasureUnit> findByName(String string);

}
