package com.shopme.site.test.setting;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.site.setting.SettingRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {

	@Autowired SettingRepository repository;
	
	@Test
	public void listSettingTwoCategoriesTest() {
		SettingCategory catOne = SettingCategory.CURRENCY;
		SettingCategory catTwo = SettingCategory.GENERAL;
		
		List<Setting>listSettings =	repository.findByTwoCategories(catOne, catTwo);
		listSettings.forEach(System.out::println);
	}
}
