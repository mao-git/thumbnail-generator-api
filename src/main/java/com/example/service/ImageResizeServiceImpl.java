package com.example.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ImageResizeServiceImpl implements ImageResizeService {
	
	@Value("${image.folder}")
	private String imageFolder;

	@Value("#{${image.size}}")
	private Map<String, List<Integer>> imageSizes;

	public String getNewNameImage(String nameImage, String keySize) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		return FilenameUtils.getBaseName(nameImage) + "_" + LocalDateTime.now().format(formatter) + "_" + keySize + "."
				+ FilenameUtils.getExtension(nameImage);
	}

	@Override
	public boolean resizeImage(File sourceFile) {

		try {
			BufferedImage bufferedImage = ImageIO.read(sourceFile);

			for (Entry<String, List<Integer>> imageSize : imageSizes.entrySet()) {
				List<Integer> valueList = imageSize.getValue();
				// imageWidth = valueList.get(0)
				// imageHeight = valueList.get(1)
				BufferedImage outputImage = Scalr.resize(bufferedImage, Method.BALANCED, Mode.FIT_EXACT,
						valueList.get(0), valueList.get(1));
				String newFileName = getNewNameImage(sourceFile.getName(), imageSize.getKey());

				Path path = Paths.get(imageFolder, newFileName);
				File newImageFile = path.toFile();
				ImageIO.write(outputImage, FilenameUtils.getExtension(sourceFile.getName()), newImageFile);
				outputImage.flush();
			}

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
