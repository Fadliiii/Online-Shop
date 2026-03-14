package com.shopme.common.entity;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "brands")
public class Brand {

	public static final String DEFAULT_LOGO = "brand-logo.png";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 45, unique = true)
	private String name;

	@Column(nullable = false, length = 128)
	private String logo;
	
	@ManyToMany
	@JoinTable(
			name = "brands_categories",
			joinColumns = @JoinColumn(name= "brands_id"),
			inverseJoinColumns = @JoinColumn(name="category_id")
			)
	private Set<Category> categories = new HashSet<>();

	@Override
	public String toString() {
		return "Brand [id=" + id + ", name=" + name + ", logo=" + logo + ", categories=" + categories + "]";
	}


	public Brand() {
		super();
	}	

	
	public Brand(String name) {
		super();
		this.name = name;
	}


	public Brand(String name, String logo, Set<Category> categories) {
		super();
		this.name = name;
		setLogo(logo);
		this.categories = categories;
	}

	public Brand(String name, String logo) {
		super();
		this.name = name;
		this.logo = logo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		if (logo == null || logo.isBlank()) {
			this.logo = DEFAULT_LOGO;
		}else {
			this.logo = logo;
		}
		
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}
	
	public void addCategory(Category category) {
		this.categories.add(category);
	}
	
	@Transient
	public String getLogoPath() {
		if(this.id == null)return "/images/image-thumbnail.png";
		return "/brand-logos/"+this.id+"/"+this.logo;
	}
	
}
