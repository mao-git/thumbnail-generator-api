package com.example.service;

import static org.mockito.Mockito.*;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import com.example.exception.ImageExtensionException;
import com.example.model.ImageUploaded;
import com.example.repo.ImageUploadRepository;

@SpringBootTest
class ImageUploadServiceTest {

	@Mock
	private ImageUploadRepository imageUploadRepository;

	@InjectMocks
	private ImageUploadServiceImpl imageUploadService;

	@Autowired
	private ImageUploadService imageService;

	MockMultipartFile imageMock;

	@BeforeEach
	void setUp() {

		imageMock = new MockMultipartFile("image", "image-test.jpeg", MediaType.IMAGE_JPEG_VALUE,
				"image mock!".getBytes());
	}

	@Test
	void ImageExtensionExceptionTest() {
		MockMultipartFile imageDbMock = new MockMultipartFile("image", "image-test.gif", MediaType.IMAGE_GIF_VALUE,
				"image throws mock!".getBytes());
		assertThrows(ImageExtensionException.class, () -> imageUploadService.uploadToDb(imageDbMock));
		
		MockMultipartFile imageLocalMock = new MockMultipartFile("image", "image-test.gif", MediaType.IMAGE_GIF_VALUE,
				"image throws mock!".getBytes());
		assertThrows(ImageExtensionException.class, () -> imageUploadService.uploadToLocal(imageLocalMock));
	}

	@Test
	void uploadToDbTest() throws IOException {
		// given
		Integer imageId = 123;
		ImageUploaded imageExpected = new ImageUploaded();
		imageExpected.setImageData(imageMock.getBytes());
		imageExpected.setImageType(imageMock.getContentType());
		imageExpected.setImageName(imageMock.getOriginalFilename());
		imageExpected.setImageId(imageId);

		when(imageUploadRepository.save(any(ImageUploaded.class))).then(new Answer<ImageUploaded>() {
			@Override
			public ImageUploaded answer(InvocationOnMock invocation) throws Throwable {
				ImageUploaded image = invocation.getArgument(0);
				image.setImageId(imageId);
				return image;
			}
		});

		// when
		ImageUploaded imageActual = imageUploadService.uploadToDb(imageMock);

		// then
		assertNotNull(imageActual);
		assertEquals(imageExpected.getImageId(), imageActual.getImageId());
		assertEquals(imageExpected.getImageName(), imageActual.getImageName());
		assertEquals(imageExpected.getImageType(), imageActual.getImageType());
		assertEquals(imageExpected.getImageData(), imageActual.getImageData());
		verify(imageUploadRepository).save(any(ImageUploaded.class));
	}

	@Test
	void uploadToLocalTest() {
		File fileReturn = imageService.uploadToLocal(imageMock);

		assertNotNull(fileReturn);
		assertEquals(fileReturn.getName(), imageMock.getOriginalFilename());
		assertEquals(FilenameUtils.getExtension(fileReturn.getName()),
				FilenameUtils.getExtension(imageMock.getOriginalFilename()));
	}
}
