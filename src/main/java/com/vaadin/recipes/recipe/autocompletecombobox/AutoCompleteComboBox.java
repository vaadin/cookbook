package com.vaadin.recipes.recipe.autocompletecombobox;

import com.vaadin.flow.component.combobox.ComboBox;

/**
 * A custom ComboBox component that provides auto-complete functionality.
 * When the user enters text, the component attempts to match the input
 * with one of the available items in the ComboBox based on the configured
 * item label generator.
 *
 * If only one match is found, it is automatically selected. Otherwise, the
 * ComboBox is cleared.
 *
 * This combo box supports only list data (values set by setItems(Collection<T>) or setItems(T...)).
 *
 * This implementation is based on Vaadin 24.
 *
 * Consider using https://vaadin.com/directory/component/autoselectcombobox addon instead, for a more advanced solution.
 *
 * @param <T> the type of items in the ComboBox
 */
public class AutoCompleteComboBox<T> extends ComboBox<T> {

    public AutoCompleteComboBox() {
        addCustomValueSetListener(event -> {
           var text = event.getDetail();

           var generator = getItemLabelGenerator();
           var matchingItems = getListDataView().getItems().filter(items ->
                   generator.apply(items).toLowerCase().contains(text.toLowerCase()))
                   .toList();
           if (matchingItems.size() == 1) {
               setValue(matchingItems.get(0));
           } else {
               clear();
           }
        });
    }
}