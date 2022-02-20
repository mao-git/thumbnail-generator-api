package com.example.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.exception.ImageExtensionException;
import com.example.model.ImageUploaded;
import com.example.repo.ImageUploadRepository;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

	@Autowired
	private ImageUploadRepository imageUploadRepository;

	@Value("${image.folder}")
	private String imageFolder;

	@Override
	public File uploadToLocal(MultipartFile multiPartFile) {

		if (!(multiPartFile.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)
				|| multiPartFile.getContentType().equals(MediaType.IMAGE_PNG_VALUE))) {
			throw new ImageExtensionException();
		}
		
		try {
			Path path = Paths.get(imageFolder, multiPartFile.getOriginalFilename());
			Files.write(path, multiPartFile.getBytes());
			return path.toFile();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ImageUploaded uploadToDb(MultipartFile multiPartFile) {

		if (!(multiPartFile.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)
				|| multiPartFile.getContentType().equals(MediaType.IMAGE_PNG_VALUE))) {
			throw new ImageExtensionException();
		}
		
		ImageUploaded imageUploaded = new ImageUploaded();
		try {
			imageUploaded.setImageData(multiPartFile.getBytes());
			imageUploaded.setImageType(multiPartFile.getContentType());
			imageUploaded.setImageName(multiPartFile.getOriginalFilename());

			ImageUploaded imageReturn = imageUploadRepository.save(imageUploaded);

			return imageReturn;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
