package nl.cookplanner.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.cookplanner.model.ShoppingItem;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, Long>{

}
