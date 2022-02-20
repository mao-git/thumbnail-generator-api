package com.example.rest;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.service.ImageResizeService;
import com.example.service.ImageUploadService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@ApiResponse
@RestController
@RequestMapping("/images")
public class ImageUploadRestController {

	@Autowired
	private ImageUploadService imageUploadService;
	
	@Autowired
	private ImageResizeService imageResizeService;

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Thumbnails generados OK de acuerdo a las dimensiones definidas en image.size del properties."),
			@ApiResponse(responseCode = "400", description = "Imagen no enviada para generar los thumbnails.")})
	@PostMapping("/resize")
	public ResponseEntity<String> resizeImage(@RequestParam("image") MultipartFile imageFile) {

		if (imageFile.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se envió ninguna imagen.");
		}
		
		File file = imageUploadService.uploadToLocal(imageFile);
		if (file == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al subir la imagen.");
		}

		boolean resizeResult = imageResizeService.resizeImage(file);
		if (!resizeResult) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al redimensionar la imagen.");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body("Se generaron los thumbnails OK.");
	}

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", 
					description = "Imagen subida correctamente a la Base de Datos H2."),
			@ApiResponse(responseCode = "400", description = "Imagen no enviada para generar los thumbnails.")})
	@PostMapping("/upload")
	public ResponseEntity<String> uploadImageDb(@RequestParam("image") MultipartFile imageFile) {

		if (imageFile.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se envió ninguna imagen.");
		}
		
		imageUploadService.uploadToDb(imageFile);
		
		return ResponseEntity.status(HttpStatus.OK).body("Imagen subida correctamente.");
	}
}
