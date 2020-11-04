package nl.cookplanner.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.IngredientName;
import nl.cookplanner.model.IngredientType;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.model.ShopType;
import nl.cookplanner.model.ShoppingItem;
import nl.cookplanner.repositories.ShoppingItemRepository;

@Slf4j
@Service
public class ShoppingService {
	
	public final PlanBoardService planBoardService;
	public final ShoppingItemRepository shoppingItemRepository;
	
	private List<ShoppingItem> shoppingList = new ArrayList<>();
	private List<ShoppingItem> stockList = new ArrayList<>();
	
	public ShoppingService(PlanBoardService planBoardService, ShoppingItemRepository shoppingItemRepository) {
		this.planBoardService = planBoardService;
		this.shoppingItemRepository = shoppingItemRepository;
	}
	
	/**
	 * The shoppingList is a non-persistent list and will be recreated on entering the list/prepare page
	 * As a convenience each shoppingItem will keep its index in the list as id
	 * @param stock
	 * @return
	 */
	public List<ShoppingItem> getShoppingListFromPlannedRecipes() {
		List<Ingredient> ingredientList = getIngredientList(false);
		shoppingList =  new ArrayList<>();
		Long index = 0L;
		for (Ingredient ingredient : ingredientList) {
			ShoppingItem shoppingItem = createShoppingItemFromIngredient(ingredient, true);
			shoppingItem.setId(index++);
			shoppingList.add(shoppingItem);
		}
		return shoppingList;
	}
	
	/**
	 * The stockList is a non-persistent list and will be recreated on entering the list/prepare page
	 * As a convenience each shoppingItem will keep its index in the list as id
	 * @param stock
	 * @return
	 */
	public List<ShoppingItem> getStockListFromPlannedRecipes() {
		List<Ingredient> ingredientList = getIngredientList(true);
		stockList =  new ArrayList<>();
		Long index = 0L;
		for (Ingredient ingredient : ingredientList) {
			ShoppingItem shoppingItem = createShoppingItemFromIngredient(ingredient, false);
			shoppingItem.setId(index++);
			stockList.add(shoppingItem);
		}
		return stockList;
	}

	/**
	 * The standard list is a persistent list and uses the database id's
	 * @param stock
	 * @return
	 */
	public List<ShoppingItem> getStandardList() {
		return shoppingItemRepository.findAll();
	}
	
	public List<ShoppingItem> getCurrentShoppingList() {
		return shoppingList;
	}
	
	public List<ShoppingItem> getCurrentStockList() {
		return stockList;
	}
	
	/**
	 * Simple version for the printed shoppingList, this will be more distinct later
	 * @return
	 */
	public List<ShoppingItem> getShoppingListTotal() {
		List<ShoppingItem> shoppingListTotal = new ArrayList<>();
		for (ShoppingItem shoppingItem : shoppingList) {
			if (shoppingItem.isOnList()) {
				shoppingListTotal.add(shoppingItem);
			}
		}
		for (ShoppingItem shoppingItem : stockList) {
			if (shoppingItem.isOnList()) {
				shoppingListTotal.add(shoppingItem);
			}
		}
		for (ShoppingItem shoppingItem : getStandardList()) {
			if (shoppingItem.isOnList()) {
				shoppingListTotal.add(shoppingItem);
			}
		}
		return shoppingListTotal;
	}
	
	/**
	 * This method retrieves the ingredients from all planned recipes and groups same ingredients together in result ingredient
	 * 
	 * @param stock
	 * @return
	 */
	public List<Ingredient> getIngredientList(boolean stock) {
		List<Ingredient> ingredients = getShoppingListIngredients(stock);
		List<Ingredient> result = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			boolean exists = false; 
			for (Ingredient resultIngredient : result) {
				if (ingredient.getName().equals(resultIngredient.getName()) && ingredient.getMeasureUnit().equals(resultIngredient.getMeasureUnit())) {
					exists = true;
					if (ingredient.getAmount() != null && resultIngredient.getAmount() != null) {
						resultIngredient.setAmount(ingredient.getAmount() + resultIngredient.getAmount());
					}
				}
			}
			if (!exists) {
				result.add(new Ingredient(ingredient.getName(),
						ingredient.getAmount(),
						ingredient.getMeasureUnit()));
			}
		}
		return result;
	}
	
	public List<Ingredient> getShoppingListIngredients(boolean isStock) {
		List<Ingredient> ingredients = new ArrayList<>();
		List<Planning> planningList = planBoardService.getPlanningList();
		for (Planning planning : planningList) {
			if (planning.isOnShoppingList()) {
				for (Recipe recipe : planning.getRecipes()) {
					for (Ingredient ingredient : recipe.getIngredients()) {
						if (ingredient.getName().isStock() == isStock) {
							ingredients.add(ingredient);
						}
					}
					
				}
			}
		}
		return ingredients;
	}
	
	public void removeFromShoppingList(int id) {
		shoppingList.get(id).setOnList(false);
	}
	
	public void addToShoppingList(int id) {
		shoppingList.get(id).setOnList(true);
	}
	
	public void removeFromStockList(int id) {
		stockList.get(id).setOnList(false);
	}
	
	public void addToStockList(int id) {
		stockList.get(id).setOnList(true);
	}
	
	public void removeFromStandardList(Long id) {
		Optional<ShoppingItem> optionalShoppingItem = shoppingItemRepository.findById(id);
		if (optionalShoppingItem.isPresent()) {
			ShoppingItem shoppingItem = optionalShoppingItem.get();
			shoppingItem.setOnList(false);
			shoppingItemRepository.save(shoppingItem);
		}
	}
	
	public void addToStandardList(Long id) {
		Optional<ShoppingItem> optionalShoppingItem = shoppingItemRepository.findById(id);
		if (optionalShoppingItem.isPresent()) {
			ShoppingItem shoppingItem = optionalShoppingItem.get();
			shoppingItem.setOnList(true);
			shoppingItemRepository.save(shoppingItem);
		}
	}
	
	private ShoppingItem createShoppingItemFromIngredient(Ingredient ingredient, boolean onList) {
		ShoppingItem shoppingItem = new ShoppingItem();
		shoppingItem.setAmount(ingredient.getAmount());
		shoppingItem.setMeasureUnit(ingredient.getMeasureUnit());
		shoppingItem.setName(ingredient.getName());
		shoppingItem.setStandard(false);
		shoppingItem.setOnList(onList);
		shoppingItem.setIngredientType(ingredient.getName().getIngredientType() == null ? IngredientType.OVERIG : ingredient.getName().getIngredientType()) ;
		shoppingItem.setShopType(ingredient.getName().getShopType() == null ? ShopType.OVERIG : ingredient.getName().getShopType());
		return shoppingItem;
	}
}
