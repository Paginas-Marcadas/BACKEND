package com.maria.paginas_marcadas.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.maria.paginas_marcadas.entity.Usuario;

@Service
public class CloudinaryService {

	@Autowired
	private Cloudinary cloudinary;
	
	//ADICIONANDO UMA IMAGEM VINCULADA AO ID_USUARIO NO CLOUDINARY
	public String uploadAvatarFile (MultipartFile file, Usuario usuario) throws IOException {
	    String fileType = file.getContentType();

	    if (fileType == null || !fileType.startsWith("image")) {
	        throw new IllegalArgumentException("Arquivo não é uma imagem válida.");
	    }

	    Map<String, Object> options = new HashMap<>();

	    options.put("resource_type", "image");
	    options.put("public_id", "usuarios/avatar/" + usuario.getId());  
	    options.put("folder", "usuarios/avatar");

	    Map<String, Object> transformationImage = new HashMap<>();
	    transformationImage.put("width", 300);
	    transformationImage.put("height", 300);
	    transformationImage.put("crop", "fill");
	    transformationImage.put("quality", "auto");
	    transformationImage.put("format", "jpg");

	    options.put("transformation", transformationImage);
	    options.put("tags", new String[]{"avatar", "usuario-" + usuario.getId()});

	    @SuppressWarnings("rawtypes")
	    Map resultUpload = cloudinary.uploader().upload(file.getBytes(), options);

	    return resultUpload.get("secure_url").toString();
	}
	
	//ADICIONANDO UM ARQUIVO (IMAGEM OU VÍDEO) NO CLOUDINARY
	public String uploadMemoryFile (MultipartFile file, String tipo) throws IOException {
		
		Map<String, Object> options = new HashMap<>();
		
		options.put("folder", "memoria/"+tipo);
		
		String fileType = file.getContentType();
		
		if (fileType != null && fileType.startsWith("video")) {
            options.put("resource_type", "video");
            
            Map<String, Object> transformationVideo = new HashMap<>();
            transformationVideo.put("height", 300);
            transformationVideo.put("crop", "limit");
            transformationVideo.put("quality", "auto");
            transformationVideo.put("format", "mp4");
            
            options.put("tags", new String[]{"video", tipo});
            options.put("transformation", transformationVideo);    
        } 
		
		else if (fileType != null && fileType.startsWith("image")) {
	        options.put("resource_type", "image");
	        
	        Map<String, Object> transformationImage = new HashMap<>();
	        transformationImage.put("width", 300);
	        transformationImage.put("height", 300);
	        transformationImage.put("crop", "fill");
	        transformationImage.put("quality", "auto");
	        transformationImage.put("format", "jpg");
	        
	        options.put("tags", new String[]{"image", tipo});
	        options.put("transformation", transformationImage);
	        
	    } 
		
		else { throw new IllegalArgumentException("Tipo de arquivo não suportado: " + fileType); }
		
		@SuppressWarnings("rawtypes")
		Map resultUpload = cloudinary.uploader().upload(file.getBytes(), options);
		
		return resultUpload.get("secure_url").toString();
	}
	
	//REMOVENDO UM ARQUIVO DO CLOUDINARY
	public void deleteFile (String linkFile) throws IOException {
	    if (linkFile == null || linkFile.isEmpty()) {throw new IllegalArgumentException("URL inválida");}

	    String resourceType;
	    
	    if (linkFile.contains("/video/")) {
	        resourceType = "video";
	    } 
	    
	    else if (linkFile.contains("/image/")) {
	        resourceType = "image";
	    } 
	    
	    else {throw new IllegalArgumentException("Tipo de arquivo não identificado na URL"); }

	    String pathBase = linkFile.substring(linkFile.indexOf(resourceType) + resourceType.length() + 1);
	    pathBase = pathBase.substring(0, pathBase.lastIndexOf(".")); 

	    Map<String, Object> options = new HashMap<>();
	    options.put("resource_type", resourceType);

	    cloudinary.uploader().destroy(pathBase, options);
	}
}
