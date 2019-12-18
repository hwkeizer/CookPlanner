package nl.cookplanner.services;

import java.util.Set;

import nl.cookplanner.model.Tag;

public interface TagService {

	Tag findById(Long id);
	
	Tag save(Tag tag);
	
	Set<Tag> findAll();
}
