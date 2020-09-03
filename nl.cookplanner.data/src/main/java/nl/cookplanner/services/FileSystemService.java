package nl.cookplanner.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {
	
	public void uploadImage(MultipartFile image, Long id);
	
	public byte[] getImageFile(String name);
}
