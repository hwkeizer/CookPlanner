package nl.cookplanner.services;

import java.util.Set;

import nl.cookplanner.model.MeasureUnit;

public interface MeasureUnitService {

	Set<MeasureUnit> findAllMeasureUnits();
	
	MeasureUnit findMeasureUnitById(Long id);
}
