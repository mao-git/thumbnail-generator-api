package com.example.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import com.example.model.ImageUploaded;

public interface ImageUploadService {
	
	public File uploadToLocal(MultipartFile multiPartFile);
	
	public ImageUploaded uploadToDb(MultipartFile multiPartFile);
}
