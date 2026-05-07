package com.shopme.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.currency.CurrencyRepository;
import com.shopme.admin.currency.CurrencyService;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {

	@Autowired private SettingService settingService;
	
	@Autowired private CurrencyService currencyService;
	
	@Autowired private CurrencyRepository currencyRepository;
	
	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting>listSettings = settingService.listAllSetting();
		//List<Currency>listCurrencies=currencyService.lisCurrencyByName();
		List<Currency>listCurrencies = currencyRepository.findAll();
		
		model.addAttribute("listCurrencies", listCurrencies);
		
		for (Setting settings : listSettings) {
			model.addAttribute(settings.getKey(), settings.getValue());
		}
		
		return "setting/settings";
	}
	
	
	@PostMapping("/settings/save_general")
	public String saveGenaralSettings(@RequestParam("fileImage")MultipartFile multipartFile,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws IOException {
		GeneralSettingBag settingBag = settingService.getGenarlSettings();
		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);
	
		updateSettingValuesFromForm(request, settingBag.list());
		
		redirectAttributes.addFlashAttribute("message","General settings have been saved.");
		return "redirect:/settings";
	}


	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value ="/site-logo/"+fileName;
			settingBag.updateSiteLogo(value);
			
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);	
		}
	}
	
	private void saveCurrencySymbol(HttpServletRequest request,
			GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
//		Currency currency =	currencyService.findById(currencyId);
	
		Optional<Currency> findByIdResult = currencyRepository.findById(currencyId);
		if(findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}
	
	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> liSettings) {
		for (Setting setting : liSettings) {
			String value = request.getParameter(setting.getKey());

			if( value != null) {
				setting.setValue(value);
			}
		}
		settingService.saveAll(liSettings);
	}
}
