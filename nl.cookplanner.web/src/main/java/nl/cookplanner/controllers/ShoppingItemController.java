package nl.cookplanner.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.IngredientType;
import nl.cookplanner.model.MeasureUnit;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.ShopType;
import nl.cookplanner.model.ShoppingItem;
import nl.cookplanner.repositories.ShoppingItemRepository;
import nl.cookplanner.services.IngredientNameService;
import nl.cookplanner.services.IngredientService;
import nl.cookplanner.services.MeasureUnitService;
import nl.cookplanner.services.ShoppingService;
@Slf4j
@Controller
@RequestMapping("shopping-item")
public class ShoppingItemController extends AbstractController {

	private final ShoppingItemRepository shoppingItemRepository;
	private final ShoppingService shoppingService;
	private final MeasureUnitService measureUnitService;
	private final IngredientNameService ingredientNameService;



	public ShoppingItemController(ShoppingItemRepository shoppingItemRepository, ShoppingService shoppingService,
			MeasureUnitService measureUnitService,
			IngredientNameService ingredientNameService) {
		this.shoppingItemRepository = shoppingItemRepository;
		this.shoppingService = shoppingService;
		this.measureUnitService = measureUnitService;
		this.ingredientNameService = ingredientNameService;
	}

	/**
	 * Start a new shopping list preparation (with new shopping list and stock list)
	 */
	@GetMapping("/list/prepare-new-list")
	public String showShoppingListPreparationNew(Model model) {
		List<ShoppingItem> shoppingList = shoppingService.getShoppingListFromPlannedRecipes();
		List<ShoppingItem> stockList = shoppingService.getStockListFromPlannedRecipes();
		List<ShoppingItem> standardList = shoppingService.getStandardList();
		model.addAttribute("shoppingList", shoppingList);
		model.addAttribute("stockList", stockList);
		model.addAttribute("standardList", standardList);
		return "shopping-item/prepare-shoppinglist";
	}
	
	/**
	 * Show the shopping list preparation (with current shopping list and stock list
	 */
	@GetMapping("/list/prepare")
	public String showShoppingListPreparation(Model model) {
		List<ShoppingItem> shoppingList = shoppingService.getCurrentShoppingList();
		List<ShoppingItem> stockList = shoppingService.getCurrentStockList();
		List<ShoppingItem> standardList = shoppingService.getStandardList(); 
		model.addAttribute("shoppingList", shoppingList);
		model.addAttribute("stockList", stockList);
		model.addAttribute("standardList", standardList);
		return "shopping-item/prepare-shoppinglist";
	}
	
	/**
	 * Print the complete shoppingList
	 * @param shoppingList
	 * @param model
	 * @return
	 */
	@GetMapping("/print-list") 
	public String printShoppingList(Model model) {
		List<ShoppingItem> shoppingListTotal = shoppingService.getShoppingListTotal();
		log.debug("markt");
		model.addAttribute("markt", getSubList(ShopType.MARKT, IngredientType.OVERIG, true, shoppingListTotal));
		log.debug("eko");
		model.addAttribute("eko", getSubList(ShopType.EKO, IngredientType.OVERIG, true, shoppingListTotal));
		log.debug("deka_groente");
		model.addAttribute("deka_groente", getSubList(ShopType.DEKA, IngredientType.GROENTE, false, shoppingListTotal));
		log.debug("deka_zuivel");
		model.addAttribute("deka_zuivel", getSubList(ShopType.DEKA, IngredientType.ZUIVEL, false, shoppingListTotal));
		log.debug("deka_overig");
		model.addAttribute("deka_overig", getSubList(ShopType.DEKA, IngredientType.OVERIG, false, shoppingListTotal));
		log.debug("overig");
		model.addAttribute("overig", getSubList(ShopType.OVERIG, IngredientType.OVERIG, true, shoppingListTotal));
		return "shopping-item/print-shoppinglist";
	}
	
	@GetMapping("/create-standard-item")
	public String showCreateShoppingItemForm(Model model) {
		ShoppingItem shoppingItem = new ShoppingItem();
		shoppingItem.setMeasureUnit(new MeasureUnit());
		shoppingItem.setName(new IngredientName());
		model.addAttribute("shoppingItem", shoppingItem);
		model.addAttribute("measureUnitList", measureUnitService.findAllMeasureUnits());
		model.addAttribute("ingredientNameList", ingredientNameService.findAllIngredientNames());
		return "shopping-item/create-standard-item";
	}
	
	@PostMapping("/create-standard-item")
	public String createShoppingItem(@ModelAttribute("shoppingItem") ShoppingItem shoppingItem) {
		// Set measureUnit and ingredientName based on their id's
		// TODO: this should be done differently, shoppingItems should have the full measureUnit and ingredientName already!
		// The same issue exists in ingredientController:createIngredient
		shoppingItem.setMeasureUnit(measureUnitService.findMeasureUnitById(shoppingItem.getMeasureUnit().getId()));
		shoppingItem.setName(ingredientNameService.findIngredientNameById(shoppingItem.getName().getId()));
		shoppingItem.setStandard(true);
		shoppingItem.setOnList(true);
		shoppingItem.setIngredientType(shoppingItem.getIngredientType());
		shoppingItem.setShopType(shoppingItem.getShopType());
		ShoppingItem savedShoppingItem = shoppingItemRepository.save(shoppingItem);
		log.debug("ShoppingItem ingredient: {}", savedShoppingItem);
		return "redirect:/shopping-item/list/prepare";
	}
	
	@GetMapping("{id}/delete")
	public String deleteShoppingItem(@PathVariable String id) {
		Optional<ShoppingItem> optionalShoppingItem = shoppingItemRepository.findById(Long.valueOf(id));
		if (optionalShoppingItem.isPresent()) {
			ShoppingItem shoppingItem = optionalShoppingItem.get();
//			Long ingredientId = shoppingItem.getIngredient().getId();
			shoppingItemRepository.delete(shoppingItem);
//			ingredientService.deleteIngredient(ingredientId);
		}
		return "redirect:/shopping-item/list/prepare";
	}
	
	@GetMapping("/{shoppingId}/remove_from_shoppinglist")
	public String removeFromShoppingList(@PathVariable String shoppingId) {
			shoppingService.removeFromShoppingList(Integer.valueOf(shoppingId));
		return "redirect:/shopping-item/list/prepare";
	}
	
	@GetMapping("/{shoppingId}/add_to_shoppinglist")
	public String addToShoppingList(@PathVariable String shoppingId) {
		shoppingService.addToShoppingList(Integer.valueOf(shoppingId));
		return "redirect:/shopping-item/list/prepare";
	}
	
	@GetMapping("/{shoppingId}/remove_from_stocklist")
	public String removeFromStockList(@PathVariable String shoppingId) {
			shoppingService.removeFromStockList(Integer.valueOf(shoppingId));
		return "redirect:/shopping-item/list/prepare";
	}
	
	@GetMapping("/{shoppingId}/add_to_stocklist")
	public String addToStockList(@PathVariable String shoppingId) {
			shoppingService.addToStockList(Integer.valueOf(shoppingId));
		return "redirect:/shopping-item/list/prepare";
	}
	
	@GetMapping("/{shoppingId}/remove_from_standardlist")
	public String removeFromStandardList(@PathVariable String shoppingId) {
			shoppingService.removeFromStandardList(Long.valueOf(shoppingId));
		return "redirect:/shopping-item/list/prepare";
	}
	
	@GetMapping("/{shoppingId}/add_to_standardlist")
	public String addToStandardList(@PathVariable String shoppingId) {
			shoppingService.addToStandardList(Long.valueOf(shoppingId));
		return "redirect:/shopping-item/list/prepare";
	}
	
	private List<ShoppingItem> getSubList(ShopType shopType, IngredientType ingredientType, boolean all, List<ShoppingItem> shoppingList) {
		List<ShoppingItem> resultList = new ArrayList<>();
		for (ShoppingItem shoppingItem : shoppingList) {
			log.debug("FIRST: {}", shoppingItem);
			if (shoppingItem.getShopType().equals(shopType)) {
				log.debug("SECOND: {}", shoppingItem);
				if(all) {
					log.debug("THIRD: {}", shoppingItem);
					resultList.add(shoppingItem);
				} else {
					log.debug("FOURTH: {}", shoppingItem);
					if (shoppingItem.getIngredientType().equals(ingredientType)) {
						resultList.add(shoppingItem);
					}
				}
			}
		}

		log.debug("{}", resultList);
		return resultList;
	}
}
