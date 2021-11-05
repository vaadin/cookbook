package com.vaadin.recipes.recipe.treegridwithicons;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class FileItemData {

	private static final List<FileItem> ITEMS = new ArrayList<>();
	private static final String FILE_ICON = "vaadin:file-o";
	private static final String IMAGE_ICON = "vaadin:file-picture";
	private static final String TEXT_ICON = "vaadin:file-text-o";
	private static final String SHEET_ICON = "vaadin:file-table";

	static {
		ITEMS.add(createFolder(1, "My documents", null));
		ITEMS.add(createFile(11, "File", FILE_ICON, getFile(1)));
		ITEMS.add(createFile(12, "Another File.txt", TEXT_ICON, getFile(1)));
		ITEMS.add(createFile(13, "Sales Report.xls", SHEET_ICON, getFile(1)));

		ITEMS.add(createFolder(2, "Pictures", null));
		ITEMS.add(createFile(21, "0001.png", IMAGE_ICON, getFile(2)));
		ITEMS.add(createFile(22, "0002.png", IMAGE_ICON, getFile(2)));
		ITEMS.add(createFile(23, "0003.png", IMAGE_ICON, getFile(2)));
	}

	private static FileItem createFolder(int id, String name, FileItem parent) {
		return new FileItem(id, name, "vaadin:folder-o", parent, null, null);
	}

	private static FileItem createFile(int id, String name, String icon, FileItem parent) {
		return new FileItem(id, name, icon, parent, randomDate(), randomSize());
	}

	private static LocalDate randomDate() {
		// random date 3 years in the past
		int random = (int) Math.abs(Math.random() * 365 * 3);
		return LocalDate.now().minus(random, ChronoUnit.DAYS);
	}

	private static int randomSize() {
		// random size between 0 and 42 MB
		return (int) Math.abs(Math.random() * Math.random() * 42 * 1024 * 1024);
	}

	private static FileItem getFile(int id) {
		return ITEMS.stream().filter(item -> item.getId() == id).findFirst().get();
	}

	public List<FileItem> getFiles() {
		return ITEMS;
	}

	public List<FileItem> getRootFiles() {
		return ITEMS
				.stream().filter(file -> file.getParent() == null).collect(Collectors.toList());
	}

	public List<FileItem> getChildFiles(FileItem parent) {
		return ITEMS
				.stream().filter(file -> Objects.equals(file.getParent(), parent)).collect(Collectors.toList());
	}

}