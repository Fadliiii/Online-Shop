package com.shopme.common.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "countries")
public class Country {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name",length = 45,nullable = false)
	private String name;
	
	@Column(name = "code",length = 5,nullable = false)
	private String code;
	
	@OneToMany(mappedBy = "country")
	@Column(name = "states")
	private Set<State> states;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	
	public Country(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Country(String name) {
		super();
		this.name = name;
	}

	public Country(Integer id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public Country(String name, String code) {
		super();
		this.name = name;
		this.code = code;
	}

	public Country(String name, String code, Set<State> states) {
		super();
		this.name = name;
		this.code = code;
		this.states = states;
	}

	public Country() {
		super();
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + ", code=" + code +  "]";
	}
	
	
	
}
