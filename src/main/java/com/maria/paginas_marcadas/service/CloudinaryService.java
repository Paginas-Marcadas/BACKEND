package com.maria.paginas_marcadas.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.maria.paginas_marcadas.entity.Usuario;

@Service
public class CloudinaryService {

	@Autowired
    private Cloudinary cloudinary;

    public String uploadAvatarFile(MultipartFile file, Usuario usuario) throws IOException {
        String fileType = file.getContentType();

        if (fileType == null || !fileType.startsWith("image")) {
            throw new IllegalArgumentException("Arquivo não é uma imagem válida.");
        }

        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", "image");
        options.put("public_id", "usuarios/avatar/" + usuario.getId());
        options.put("folder", "usuarios/avatar");
        options.put("tags", new String[] { "avatar", "usuario-" + usuario.getId() });

        @SuppressWarnings("rawtypes")
        Map resultUpload = cloudinary.uploader().upload(file.getBytes(), options);

        String publicId = (String) resultUpload.get("public_id");
        Integer version = (Integer) resultUpload.get("version");
        
        String urlTransformada = cloudinary.url()
                .transformation(new Transformation()
                    .width(300)
                    .height(300)
                    .crop("fill")
                    .gravity("faces")
                    .quality("auto")
                    .fetchFormat("auto"))
                .version(version) // força pegar a versão mais recente
                .secure(true)
                .generate(publicId);

        return urlTransformada;
    }

    public String uploadMemoryFile(MultipartFile file, String tipo) throws IOException {
        String fileType = file.getContentType();

        Map<String, Object> options = new HashMap<>();
        options.put("folder", "memoria/" + tipo);

        if (fileType != null && fileType.startsWith("video")) {
            options.put("resource_type", "video");
        } else if (fileType != null && fileType.startsWith("image")) {
            options.put("resource_type", "image");
        } else {
            throw new IllegalArgumentException("Tipo de arquivo não suportado: " + fileType);
        }

        @SuppressWarnings("rawtypes")
        Map resultUpload = cloudinary.uploader().upload(file.getBytes(), options);

        String publicId = (String) resultUpload.get("public_id");

        if (fileType != null && fileType.startsWith("video")) {
            String urlTransformada = cloudinary.url()
                .resourceType("video")
                .transformation(new Transformation()
                    .height(300)
                    .crop("fill")
                    .gravity("auto")
                    .quality("auto")
                    .fetchFormat("auto"))
                .secure(true)
                .generate(publicId);

            return urlTransformada;
        } else {
            String urlTransformada = cloudinary.url()
                .transformation(new Transformation()
                    .width(300)
                    .height(300)
                    .crop("fill")
                    .gravity("auto")
                    .quality("auto")
                    .fetchFormat("auto"))
                .secure(true)
                .generate(publicId);

            return urlTransformada;
        }
    }

    public void deleteFile(String linkFile) throws IOException {
        if (linkFile == null || linkFile.isEmpty()) {
            throw new IllegalArgumentException("URL inválida");
        }

        String resourceType;

        if (linkFile.contains("/video/")) {
            resourceType = "video";
        } else if (linkFile.contains("/image/")) {
            resourceType = "image";
        } else {
            throw new IllegalArgumentException("Tipo de arquivo não identificado na URL");
        }

        String pathBase = linkFile.substring(linkFile.indexOf(resourceType) + resourceType.length() + 1);
        pathBase = pathBase.substring(0, pathBase.lastIndexOf("."));

        Map<String, Object> options = new HashMap<>();
        options.put("resource_type", resourceType);

        cloudinary.uploader().destroy(pathBase, options);
    }

}
