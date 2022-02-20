package com.example.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ImageSizeException {

	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxImageSize;

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("La imagen supera el tamaño máximo permitido de: " + maxImageSize);
	}
}
