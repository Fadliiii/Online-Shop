package com.shopme.admin.paging;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface SearchRepository<T,ID> extends JpaRepository<T, ID>{
	
	public Page<T> findAll(String keyword,Pageable pageable);
}
