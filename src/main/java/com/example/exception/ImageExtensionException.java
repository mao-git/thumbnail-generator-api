package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImageExtensionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2500165754685884653L;

	@ExceptionHandler(ImageExtensionException.class)
	public ResponseEntity<String> handleImageExtensionException(ImageExtensionException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Extensión de archivo no valida.");
	}
}