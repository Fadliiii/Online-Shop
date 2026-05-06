package com.shopme.admin.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer>{

}
