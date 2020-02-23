package nl.cookplanner.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.repositories.MeasureUnitRepository;

@Controller
@RequestMapping("measure-unit")
@Slf4j
public class MeasureUnitController extends AbstractController {
	
	private final MeasureUnitRepository measureUnitRepository;

	public MeasureUnitController(MeasureUnitRepository measureUnitRepository) {
		this.measureUnitRepository = measureUnitRepository;
	}
	
	@GetMapping("list")
	public String showMeasureUnitList(Model model) {
		List<MeasureUnit> measureUnitList =measureUnitRepository.findAll();
		model.addAttribute("measureUnitList", measureUnitList);
		return "measure-unit/list";
	}
	
	@GetMapping("{id}/update")
	public String getMeasureUnitForm(Model model, @PathVariable String id) {
		Optional<MeasureUnit> measureUnit = measureUnitRepository.findById(Long.valueOf(id));
		if (measureUnit.isPresent()) {
			model.addAttribute("measureUnit", measureUnit.get());
		} 
		return "measure-unit/update";
	}
	
	@PostMapping("update")
	public String updateMeasureUnit(@Valid @ModelAttribute("measureUnit") MeasureUnit measureUnit, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {log.debug(objectError.toString());});
		}
		measureUnitRepository.save(measureUnit);
		return "redirect:/measure-unit/list";
	}
	
}
