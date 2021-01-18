package com.vaadin.recipes.recipe.scrollabledialog;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("scrollable-dialog")
@Metadata(
	howdoI = "Create a scrollable dialog",
	description = "A dialog with a fixed header, scrollable content and fixed footer.",
	sourceFiles = {
		"recipe/scrollabledialog/scrollable-dialog.css",
		"recipe/scrollabledialog/scrollable-dialog-renderer.css"
	}
)
@CssImport(
	themeFor = "vaadin-dialog-overlay",
	value = "./recipe/scrollabledialog/scrollable-dialog.css"
)
@CssImport(
	value = "./recipe/scrollabledialog/scrollable-dialog-renderer.css"
)

public class ScrollableDialog extends Recipe {

	public ScrollableDialog() {
		new NewEmployeeDialog().open();
	}

	private class NewEmployeeDialog extends Dialog {

		private H2 title;

		public NewEmployeeDialog() {
			getElement().getThemeList().add("scrollable-dialog");

			// Header
			title = new H2("New employee");
			title.setId("scrollable-dialog-title");
			Header header = new Header(title);

			// Content
			Section personalDetails = new Section(
					new H3("Personal details"),
					new TextField("First name"),
					new TextField("Last name"),
					new DatePicker("Birthdate")
			);
			Section contactInfo = new Section(
					new H3("Contact information"),
					new EmailField("Email address"),
					new TextField("Phone number")
			);
			Section employmentInfo = new Section(
					new H3("Employment information"),
					new DatePicker("Hire date"),
					new TextField("Job title")
			);
			Div content = new Div(personalDetails, contactInfo, employmentInfo);
			content.addClassName("scrollable-dialog-content");

			// Footer
			Button cancel = new Button("Cancel", e -> close());
			cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

			Button save = new Button("Save", e -> close());
			save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

			Footer footer = new Footer(cancel, save);

			// Add it all together
			add(header, content, footer);
		}

		@Override
		protected void onAttach(AttachEvent attachEvent) {
			super.onAttach(attachEvent);

			// Label the dialog (accessibility)
			getElement().executeJs("this.$.overlay.setAttribute('aria-labelledby', $0)", title.getId().get());
		}
	}

}
