package com.example.voter_engine.service;

import com.example.voter_engine.controller.commonController;
import com.example.voter_engine.repository.ImageStorageService;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageStorageServiceImpl implements ImageStorageService {
    private String rootdir="user-photos/";

    @Override
    public String saveImage(String id, MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = rootdir+ id;
        Path uploadPath = Paths.get(uploadDir);
        Path filePath = uploadPath.resolve(fileName);

        Files.deleteIfExists(filePath);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }

//        UriComponents uriComponents = UriComponentsBuilder.newInstance()
//                .scheme("http").host("localhost:8080").path("/images?id="+id+"&filename="+fileName).build();
        return "success";
    }

    public String getUserImageUrl(String id,String fileName){
                String url = MvcUriComponentsBuilder
                .fromMethodName(commonController.class, "getUserImage",id,fileName).build().toString();
        return url;
    }

    public String getCandidateImageUrl(String id,String fileName){
        String url = MvcUriComponentsBuilder
                .fromMethodName(commonController.class, "getCandidateImage",id,fileName).build().toString();
        return url;
    }

    @Override
    public UrlResource getImage(String id, String filename) {
        try {
            String uploadDir = rootdir+ id;
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(filename);
            UrlResource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
