package nl.cookplanner.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.repositories.MeasureUnitRepository;

@Service
public class MeasureUnitService {

	private final MeasureUnitRepository measureUnitRepository;
	
	public MeasureUnitService(MeasureUnitRepository measureUnitRepository) {
		this.measureUnitRepository = measureUnitRepository;
	}

	public Set<MeasureUnit> findAllMeasureUnits() {
		
		Set<MeasureUnit> measureUnitSet = new HashSet<>();
		measureUnitRepository.findAll().iterator().forEachRemaining(measureUnitSet::add);
		return measureUnitSet;
	}

	public MeasureUnit findMeasureUnitById(Long id) {
		Optional<MeasureUnit> optionalMeasureUnit = measureUnitRepository.findById(id);
		if (optionalMeasureUnit.isPresent()) {
			return optionalMeasureUnit.get();
		} else {
			// TODO implement errorhandling
		}
		return null;
	}

}
