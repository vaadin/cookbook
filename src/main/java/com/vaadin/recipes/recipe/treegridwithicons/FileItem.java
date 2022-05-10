package com.vaadin.recipes.recipe.treegridwithicons;

import java.time.LocalDate;

public class FileItem {

	private int id;
	private String name;
	private String icon;
	private FileItem parent;
	private LocalDate lastModified;
	private Integer size;

	public FileItem(int id, String name, String icon, FileItem parent, LocalDate lastModified, Integer size) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.parent = parent;
		this.lastModified = lastModified;
		this.size = size;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public FileItem getParent() {
		return parent;
	}

	public void setParent(FileItem parent) {
		this.parent = parent;
	}

	public LocalDate getLastModified() {
		return lastModified;
	}

	public void setLastModified(LocalDate lastModified) {
		this.lastModified = lastModified;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
