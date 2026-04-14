package com.shopme.admin.brand;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Service
public class BrandService {
	public static final int BRANDS_PER_PAGE = 10;
	
	@Autowired
	private BrandRepository repository;
	
	public List<Brand> listBrand(){
		return repository.findAll();
	}
	
	public Page<Brand> listByPage(int pageNum,String sortField, String sortDir, String keyword){
		
		Sort sort= Sort.by(sortField);
	
		sort = sortDir.equals("asc")?sort.ascending() : sort.descending();
		
		org.springframework.data.domain.Pageable pageable =  PageRequest.of(pageNum -1, BRANDS_PER_PAGE, sort);
		
		if (keyword!=null) {
			return repository.findAll(keyword, pageable);
		}
     	return repository.findAll(pageable);
	}
	
	public Brand save(Brand brand,MultipartFile multipartFile) throws IOException {
		
		if (multipartFile != null && !multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand brandSaved = repository.save(brand);
			
			String uploadDir = "../brand-logos/"+brandSaved.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
			return brandSaved;
		}
		if (brand.getLogo()==null || brand.getLogo().isEmpty()) {
			brand.setLogo(null);
		}
		
		return repository.save(brand);
	}
	public Brand findById(Integer id) {
		return repository.findById(id).orElseThrow(()-> new NotFoundException("brand id is not found"));
	}

	public void delete(Integer id) {
		Long countById = repository.countById(id);
		if(countById == null || countById == 0) {
			throw new NotFoundException("Could Not find any category = "+ id);
		}
		repository.deleteById(id);
	}
	
	public String checkUnique(Integer id, String name) {

	    boolean isCreatingNew = (id == null || id == 0);

	    Brand brandByName = repository.findByName(name);

	    if(isCreatingNew) {
	        if(brandByName != null) {
	            return "Duplicate";
	        }
	    } else {
	        if(brandByName != null && brandByName.getId() != id) {
	            return "Duplicate";
	        }

	    }

	    return "OK";
	}
}
