package com.example.voter_engine.repository;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageStorageService {


    public String saveImage(String id, MultipartFile multipartFile) throws IOException;

    public UrlResource getImage(String id,String filename);

    String getUserImageUrl(String id,String fileName);

    String getCandidateImageUrl(String id,String fileName);
}
