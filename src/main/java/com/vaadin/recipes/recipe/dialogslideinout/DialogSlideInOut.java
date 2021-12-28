package com.vaadin.recipes.recipe.dialogslideinout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

@Route("dialog-slide-in-out")
@Metadata(
		howdoI = "Make a dialog slide in (open) & out (close)",
		description = "Change Dialog's default animation to slide in and out from the bottom of the page when opening and closing it respectively."
)
@CssImport(
		themeFor = "vaadin-dialog-overlay",
		value = "./recipe/dialog-slide-in-out/vaadin-dialog-overlay-styles.css"
)
public class DialogSlideInOut extends Recipe {

	private Dialog dialog = new Dialog();

	public DialogSlideInOut() {
		Button open = new Button("Open dialog");
		open.addClickListener(e -> dialog.open());
		add(open);

		Button close = new Button("Close dialog");
		close.addClickListener(e -> dialog.close());
		dialog.add(close);
	}

}
