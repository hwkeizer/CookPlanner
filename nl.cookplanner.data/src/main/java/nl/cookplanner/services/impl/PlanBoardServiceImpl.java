package nl.cookplanner.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Planning;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.repositories.PlanningRepository;
import nl.cookplanner.repositories.RecipeRepository;
import nl.cookplanner.services.PlanBoardService;

@Service
@Slf4j
public class PlanBoardServiceImpl implements PlanBoardService {

	private final PlanningRepository planningRepository;
	private final RecipeRepository recipeRepository;
	// TODO: consider if it is good practise to keep some state in a service...
	private final ArrayList<Planning> planBoard = new ArrayList<>();
	
	public PlanBoardServiceImpl(PlanningRepository planningRepository, RecipeRepository recipeRepository) {
		this.planningRepository = planningRepository;
		this.recipeRepository = recipeRepository;
		ArrayList<Planning> allPlannings = (ArrayList<Planning>)planningRepository.findAll();
		for (Planning planning : allPlannings) {
			planBoard.add(planning);
		}
	}

	@Override
	public boolean savePlanBoard() {
		planningRepository.saveAll(planBoard);
		return true;
	}

	@Override
	public Iterable<Planning> getPlannings() {
//		return planningRepository.findAll();
		Collections.sort(planBoard);
		return planBoard;
	}

	@Override
	public void deletePlanningById(Long planningId) {
		planningRepository.deleteById(planningId);
		
	}
	
	// TODO: Change implementation so it won't call the database for each move (probably javascript solution)
	public void movePlanning(Long planningId, LocalDate localDate) {
		Planning planning = getPlanning(planningId);
		planning.setDate(localDate);
		planningRepository.save(planning);
		Collections.sort(planBoard);
	}
	
	@Override
	public boolean addPlanning(Recipe recipe) {
		Planning planning = new Planning(getFirstAvailableDate(), recipe, true);
		planBoard.add(planning);
		planningRepository.save(planning);
//		savePlanBoard();
		return true;
	}
	
	@Override
	public boolean addPlanning() {
		Planning planning = new Planning(getFirstAvailableDate());
		planBoard.add(planning);
		planningRepository.save(planning);
//		savePlanBoard();
		return true;
	}
	
	private LocalDate getFirstAvailableDate() {
		LocalDate localDate = LocalDate.now();
		if (planBoard.isEmpty()) return localDate;
		Collections.sort(planBoard);
		for (Planning planning : planBoard) {
			if (planning.getDate().isBefore(localDate)) continue;	
			
			if (!planning.getDate().equals(localDate)) {
				return localDate;
			} else localDate = localDate.plusDays(1);
		}
		return localDate;
	}
	
	@Override
	public boolean removePlanning(Long planningId) {
		Planning planning = getPlanning(planningId);
		planBoard.remove(planning);
		planningRepository.deleteById(planningId);
		return true;
	}

	@Override
	public Planning setOnShoppingList(Long planningId, boolean onShoppingList) {
		Planning planning = getPlanning(planningId);
		planning.setOnShoppingList(onShoppingList);
		return planningRepository.save(planning);
	}
	
	private Planning getPlanning(Long planningId) {
		for (Planning planning : planBoard) {
			if (planning.getId().equals(planningId)) return planning;
		}
		return null;
	}

	// TODO: Test this method thoroughly and check if this code can be improved
	@Override
	public List<Ingredient> getShoppingList(boolean stock) {
		List<Ingredient> ingredients = getShoppingListIngredients(stock);
		List<Ingredient> result = new ArrayList<>();
		for (Ingredient ingredient : ingredients) {
			boolean exists = false; 
			for (Ingredient resultIngredient : result) {
				if (ingredient.getName().equals(resultIngredient.getName())) {
					exists = true;
					log.debug("RESULTING INGREDIENT: {}", resultIngredient);
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
	
	
	private List<Ingredient> getShoppingListIngredients(boolean isStock) {
		// TODO: first try with streams: investigate how database calls are handled???!!!
		List<Ingredient> ingredients  = planBoard.stream()
				.filter(p -> p.getRecipe() != null)
				.filter(p -> p.isOnShoppingList())
				.map(p -> p.getRecipe().getId())
				.filter(i -> recipeRepository.findById(i).isPresent())
				.flatMap(i -> recipeRepository.findById(i).get().getIngredients().stream())
				.filter(i -> (i.getName().isStock() == isStock))
				.collect(Collectors.toList());
		
		
		return ingredients;
	}
}