package com.shopme.site.customer;

import com.shopme.site.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    private final SettingService settingService;

	@Autowired CustomerService customerService;

    CustomerRestController(SettingService settingService) {
        this.settingService = settingService;
    }
	
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(@Param("email")String email) {
		return customerService.isEmailUnique(email)?"OK" : "Duplicated";
	}
}
