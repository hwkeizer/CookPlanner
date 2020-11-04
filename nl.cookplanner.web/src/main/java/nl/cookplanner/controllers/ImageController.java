package nl.cookplanner.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import nl.cookplanner.services.FileSystemService;

@Controller
@RequestMapping("/image")
public class ImageController extends AbstractController {

	private final FileSystemService fileSystemService;

	
	public ImageController(FileSystemService fileSystemService) {
		this.fileSystemService = fileSystemService;
	}
	
	@GetMapping(value="/{name}", produces = {MediaType.IMAGE_PNG_VALUE})
	public @ResponseBody byte[] getImage(@PathVariable String name) {
		if (name.isBlank() || name.equals("null")) {
			return new byte[0];
		}
		return fileSystemService.getImageFile(name);
	}
	
	@PostMapping(value = "/upload/{id}")
	public String importImage(@RequestParam("imageFile") MultipartFile image, @PathVariable String id, Model model) {
		fileSystemService.uploadImageForRecipe(image, Long.valueOf(id));
		return "redirect:/recipe/"+ id +"/update";
	}
	
}
