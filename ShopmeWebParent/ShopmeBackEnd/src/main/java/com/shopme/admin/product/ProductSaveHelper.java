package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;

public class ProductSaveHelper {
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	static void deleteExtraImagesWeredRemoveOnForm(Product product) {

		String extraImage = "../product-images/"+product.getId()+"/extras";
		Path dirpath = Paths.get(extraImage);
		
		try {
			Files.list(dirpath).forEach(file ->{
				String fileName = file.toFile().getName();
				if(!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.info("Deleted extra image : "+fileName);
					}catch (IOException e) {
						LOGGER.error("Could not delete extra image : "+fileName);
					}
				}
			});
		}catch (IOException e) {
			LOGGER.error("Could not list directory : "+dirpath);
		}
	}

	static void setExisttingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {

		if(imageIDs == null || imageIDs.length == 0) return;
	
		Set<ProductImage> images = new HashSet<>();
		
		for(int count = 0; count < imageIDs.length; count++) {
			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			images.add(new ProductImage(id,name,product));
			
		}
		product.setImages(images);
		
	}

	static void setProductDetails(String[] detailIDs,String[] detailNames, String[] detailValues, Product product) {
		if(detailNames == null || detailNames.length == 0) return;
		
		int length = Math.min(detailNames.length, detailValues.length);
		
		for(int count = 0; count <length; count++) {
			String name = detailNames[count];
			String value = detailValues[count];
			Integer id = Integer.parseInt(detailIDs[count]);
			if(id != 0) {
				product.addDetail(id,name,value);
			}else if(!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
			
		}
	}

	static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {
		
		if(!mainImageMultipart.isEmpty()) {
			String fileName= StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
		
			String uploadDir= "../product-images/"+savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		
		if(extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) { 
				String uploadDir= "../product-images/"+savedProduct.getId()+"/extras"; 

				if(multipartFile.isEmpty()) continue;
				
				String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
}

	static void setMainImageName(MultipartFile mainImageMultipart,Product product) {
		if(!mainImageMultipart.isEmpty()) {
			String fileName= StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
	
	static void setNewExtraImagesNames(MultipartFile[] extraImageMultiparts,Product product) {
		
		if(extraImageMultiparts.length > 0) {	
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if(!multipartFile.isEmpty()) {
					String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
						if(!product.containsImageName(fileName)) {
							product.addExtraImage(fileName);
						}
				}
			}
		}
	}
}
