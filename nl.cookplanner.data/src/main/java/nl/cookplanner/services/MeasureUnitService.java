package nl.cookplanner.services.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.repositories.MeasureUnitRepository;
import nl.cookplanner.services.MeasureUnitService;

@Service
public class MeasureUnitServiceImpl implements MeasureUnitService {

	private final MeasureUnitRepository measureUnitRepository;
	
	public MeasureUnitServiceImpl(MeasureUnitRepository measureUnitRepository) {
		this.measureUnitRepository = measureUnitRepository;
	}

	@Override
	public Set<MeasureUnit> findAllMeasureUnits() {
		
		Set<MeasureUnit> measureUnitSet = new HashSet<>();
		measureUnitRepository.findAll().iterator().forEachRemaining(measureUnitSet::add);
		return measureUnitSet;
	}

	@Override
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
