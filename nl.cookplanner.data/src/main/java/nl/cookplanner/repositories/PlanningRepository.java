package nl.cookplanner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.cookplanner.model.Planning;

public interface PlanningRepository extends JpaRepository<Planning, Long> {

}
