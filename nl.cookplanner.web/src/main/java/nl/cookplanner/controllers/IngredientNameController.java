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
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.repositories.IngredientNameRepository;

@Controller
@RequestMapping("ingredient-name")
@Slf4j
public class IngredientNameController {
	
	private final IngredientNameRepository ingredientNameRepository;
	
	public IngredientNameController(IngredientNameRepository ingredientNameRepository) {
		this.ingredientNameRepository = ingredientNameRepository;
	}

	@GetMapping("list")
	public String showIngredientNameList(Model model) {
		log.debug("We are getting the list!");
		List<IngredientName> ingredientNameList =ingredientNameRepository.findAll();
		model.addAttribute("ingredientNameList", ingredientNameList);
		return "ingredient-name/list";
	}
	
	@GetMapping("{id}/update")
	public String updateIngredientNameForm(Model model, @PathVariable String id) {
		log.debug("Recieved ingredientName for update: {}", id);
		Optional<IngredientName> ingredientName = ingredientNameRepository.findById(Long.valueOf(id));
		if (ingredientName.isPresent()) {
			log.debug("ingredient: {}", ingredientName.get());
			model.addAttribute("ingredientName", ingredientName.get());
		} 
		return "ingredient-name/update";
	}
	
	@PostMapping("update")
	public String updateIngredientName(@Valid @ModelAttribute("ingredientName") IngredientName ingredientName, BindingResult bindingResult) {
		log.debug("ingredient to save: {}", ingredientName);
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {
				log.debug(objectError.toString());
			});
		}
		ingredientNameRepository.save(ingredientName);
		return "redirect:/ingredient-name/list";
	}
	
}
