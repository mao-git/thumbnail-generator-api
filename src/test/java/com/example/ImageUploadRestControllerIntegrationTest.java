package com.example;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@SpringBootTest
class ImageUploadRestControllerIntegrationTest {
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	MockMvc mvc;
	
	@BeforeEach
	public void setUp() {
		
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void uploadImageDbTest() throws Exception {
		//Test Image JPEG
		//Given
		MockMultipartFile imageJPEG = new MockMultipartFile("image", "image-test.jpeg", MediaType.IMAGE_JPEG_VALUE,
				"image mock JPEG testing".getBytes());
	
		//when
		mvc.perform(MockMvcRequestBuilders.multipart("/images/upload").file(imageJPEG))
				//Then
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
				.andExpect(content().string("Imagen subida correctamente."));
		
		//Test Image PNG
		//Given
		MockMultipartFile imagePNG = new MockMultipartFile("image", "image-test.png", MediaType.IMAGE_PNG_VALUE,
				"image mock PNG testing".getBytes());
		
		//when
		mvc.perform(MockMvcRequestBuilders.multipart("/images/upload").file(imagePNG))
				//Then
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
				.andExpect(content().string("Imagen subida correctamente."));
		
		//Test Image GIF
		//Given
		MockMultipartFile imageGIF = new MockMultipartFile("image", "image-test.png", MediaType.IMAGE_GIF_VALUE,
				"image mock GIF testing".getBytes());
		
		mvc.perform(MockMvcRequestBuilders.multipart("/images/upload").file(imageGIF))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
		.andExpect(content().string("Extensión de archivo no valida."));
	}
	
	@Test
	@Disabled
	public void resizeImage() throws Exception {
		
	}
}
