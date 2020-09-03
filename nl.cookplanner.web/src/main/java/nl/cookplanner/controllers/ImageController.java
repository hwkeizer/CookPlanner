package nl.cookplanner.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Ingredient;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.services.FileSystemService;
import nl.cookplanner.services.RecipeService;

@Controller
@Slf4j
@RequestMapping("/image")
public class ImageController extends AbstractController {

	private final FileSystemService fileSystemService;
	private final RecipeService recipeService;

	
	public ImageController(FileSystemService fileSystemService, RecipeService recipeService) {
		this.fileSystemService = fileSystemService;
		this.recipeService = recipeService;
	}
	
	@GetMapping(value="/{name}", produces = {MediaType.IMAGE_PNG_VALUE})
	public @ResponseBody byte[] getImage(@PathVariable String name) {
		log.debug("GetImage: {}", name);
		return fileSystemService.getImageFile(name);
	}
	
	@PostMapping(value = "/upload/{id}")
	public String importImage(@RequestParam("imageFile") MultipartFile image, @PathVariable String id, Model model) {
		fileSystemService.uploadImage(image, Long.valueOf(id));
		return "redirect:/recipe/"+ id +"/update";
	}
	
}
