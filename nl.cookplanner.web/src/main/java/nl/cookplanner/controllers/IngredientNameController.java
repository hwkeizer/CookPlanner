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
public class IngredientNameController extends AbstractController {
	
	private final IngredientNameRepository ingredientNameRepository;
	
	public IngredientNameController(IngredientNameRepository ingredientNameRepository) {
		this.ingredientNameRepository = ingredientNameRepository;
	}

	@GetMapping("list")
	public String showIngredientNameList(Model model) {
		List<IngredientName> ingredientNameList =ingredientNameRepository.findAll();
		model.addAttribute("ingredientNameList", ingredientNameList);
		return "ingredient-name/list";
	}
	
	@GetMapping("create")
	public String getIngredientNameCreateForm(Model model)
	{
		IngredientName ingredientName = new IngredientName();
		model.addAttribute("ingredientName", ingredientName);
		return "ingredient-name/create";
	}
	
	@PostMapping("create")
	public String createIngredientName(@Valid @ModelAttribute("ingredientName") IngredientName ingredientName, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {log.debug(objectError.toString());});
		}
		ingredientNameRepository.save(ingredientName);
		return "redirect:/ingredient-name/list";
	}
	
	@GetMapping("{id}/update")
	public String getIngredientNameUpdateForm(Model model, @PathVariable String id) {
		Optional<IngredientName> ingredientName = ingredientNameRepository.findById(Long.valueOf(id));
		if (ingredientName.isPresent()) {
			model.addAttribute("ingredientName", ingredientName.get());
		} 
		return "ingredient-name/update";
	}
	
	@PostMapping("update")
	public String updateIngredientName(@Valid @ModelAttribute("ingredientName") IngredientName ingredientName, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(objectError -> {log.debug(objectError.toString());});
		}
		ingredientNameRepository.save(ingredientName);
		return "redirect:/ingredient-name/list";
	}
	
	// TODO make this a delete mapping
	@GetMapping("{id}/delete")
	public String deleteIngredientName(@PathVariable String id) {
		// TODO Add check on in use (or throw exception when ingredientName in use)
		ingredientNameRepository.deleteById(Long.valueOf(id));
		return "redirect:/ingredient-name/list";
	}
	
}
