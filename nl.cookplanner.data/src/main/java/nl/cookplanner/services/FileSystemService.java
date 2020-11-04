package nl.cookplanner.services;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;

@Service
@Slf4j
public class FileSystemService  {

	@Value("${location.images}")
	private String imageFolder;
	
	private final RecipeService recipeService;
	
	public FileSystemService(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	public Recipe uploadImageForRecipe(MultipartFile image, Long id) {
		Recipe recipe = recipeService.findRecipeById(id);
		
		Path filePath = createPathForRecipeImage(recipe.getName(), getFileNameExtension(image.getOriginalFilename()));
		
		try (OutputStream os = Files.newOutputStream(filePath)) {
			os.write(image.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		recipe.setImage(filePath.getFileName().toString());
		return recipeService.updateRecipe(recipe);
	}
	
	
	public byte[] getImageFile(String name) {
		Path imagePath = FileSystems.getDefault().getPath(imageFolder, name);
		try {
			return Files.readAllBytes(imagePath);
		} catch(IOException ex) {
			// TODO: Implement proper exception handling
			return new byte[0];
		}
	}
	
	protected Path createPathForRecipeImage(String recipeName, String fileExtension) {
		String fileName = recipeName + fileExtension;
		return Paths.get(imageFolder, fileName);
	}
	
	protected String getFileNameExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
        	return fileName.substring(fileName.lastIndexOf("."));
        }
        else return "";
    }
}
