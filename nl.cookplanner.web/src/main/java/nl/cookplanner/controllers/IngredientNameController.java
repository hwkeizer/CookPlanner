package nl.cookplanner.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import nl.cookplanner.model.IngredientName;
import nl.cookplanner.repositories.IngredientNameRepository;

@Controller
@RequestMapping("ingredient-name")
public class IngredientNameController {

	private final IngredientNameRepository ingredientNameRepository;
	
	public IngredientNameController(IngredientNameRepository ingredientNameRepository) {
		this.ingredientNameRepository = ingredientNameRepository;
	}

	@RequestMapping("list")
	public String showIngredientNameList(Model model) {
		List<IngredientName> ingredientNameList =ingredientNameRepository.findAll();
		model.addAttribute("ingredientNameList", ingredientNameList);
		return "ingredient-name/list";
	}
}
