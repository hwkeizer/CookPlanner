package nl.cookplanner.services.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.repositories.MeasureUnitRepository;
import nl.cookplanner.services.MeasureUnitService;

@Service
@Slf4j
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

}
