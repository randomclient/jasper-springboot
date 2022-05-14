package com.exercise.model;

import javax.validation.constraints.NotEmpty;


public class ClassBean {
	@NotEmpty(message="empty field")
	private String id;
	@NotEmpty(message="empty field")
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
