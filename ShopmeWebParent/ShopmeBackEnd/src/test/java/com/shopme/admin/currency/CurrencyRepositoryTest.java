package com.shopme.admin.currency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {

	@Autowired private CurrencyRepository currencyRepository;
	
	@Test
	public void testCreateCurrencies() {
		List<Currency>currencies=Arrays.asList(
			new Currency("United States Dollar","$","USD"),
			new Currency("British Pound","£","GPB"),
			new Currency("Japanse Yen","¥","JPY"),
			new Currency("Euro","€","EUR"),
			new Currency("Russian Ruble","₽","RUB"),
			new Currency("South Korean Won","₩","KRW"),
			new Currency("Chinese Yuan","¥","CNY"),
			new Currency("Brazilian Real","R$","BRL"),
			new Currency("Australian Dollar","A$","AUD"),
			new Currency("Canadian Dollar","CA$","CAD"),
			new Currency("Vietnamese Dong","₫","VND"),
			new Currency("Indian Rupe","₹","INR"),
			new Currency("Rupiah Indonesia","Rp","IDR")
			);
		currencyRepository.saveAll(currencies);
		
		Iterable<Currency> iterable= currencyRepository.findAll();
		
		assertThat(iterable).size().isEqualTo(13);
	}
}
