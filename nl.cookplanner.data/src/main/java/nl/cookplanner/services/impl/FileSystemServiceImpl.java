package nl.cookplanner.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import nl.cookplanner.model.Recipe;
import nl.cookplanner.services.FileSystemService;
import nl.cookplanner.services.RecipeService;

@Service
@Slf4j
public class FileSystemServiceImpl implements FileSystemService {

	@Value("${location.images}")
	private String imageLocation;
	
	private final RecipeService recipeService;
	
	public FileSystemServiceImpl(RecipeService recipeService) {
		super();
		this.recipeService = recipeService;
	}

	@Override
	public void uploadImage(MultipartFile image, Long id) {
		Recipe recipe = recipeService.findRecipeById(id);
		String fileExtension = getFileExtension(image.getOriginalFilename());
		String fileName = recipe.getName() + "." + fileExtension;
		Path filePath = Paths.get(imageLocation, fileName);

		try (OutputStream os = Files.newOutputStream(filePath)) {
			os.write(image.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.debug("Filenaam: {}", filePath.getFileName());
		recipe.setImage(filePath.getFileName().toString());
		recipeService.updateRecipe(recipe);
	}
	
	private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	
	@Override
	public byte[] getImageFile(String name) {
		Path imagePath = FileSystems.getDefault().getPath(imageLocation, name);
		try {
			return Files.readAllBytes(imagePath);
		} catch(IOException ex) {
			// TODO: Implement proper exception handling
			return null;
		}
	}

}
