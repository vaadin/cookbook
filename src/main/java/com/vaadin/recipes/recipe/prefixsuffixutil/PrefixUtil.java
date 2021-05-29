package com.vaadin.recipes.recipe.prefixsuffixutil;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.dom.Element;

/**
 * Utility class to set the component to the prefix slot and get the component 
 * currently in the prefix slot. Note suffix slot could be potentially handled
 * in similar fashion, but in case this is needed for DatePicker, ComboBox and
 * Select, which have the drop down button in suffix slot, it would not be
 * actually useful.
 * 
 */
public class PrefixUtil {

    private static Stream<Element> getElementsInSlot(HasElement target,
            String slot) {
        return target.getElement().getChildren()
                .filter(child -> slot.equals(child.getAttribute("slot")));
    }

    public static void setPrefixComponent(Component target, Component component) {
        clearSlot(target,"prefix");

        if (component != null) {
            component.getElement().setAttribute("slot", "prefix");
            target.getElement().appendChild(component.getElement());
        }
    }

    private static void clearSlot(Component target, String slot) {
        getElementsInSlot(target, slot).collect(Collectors.toList())
                .forEach(target.getElement()::removeChild);
    }

    private static Component getChildInSlot(HasElement target, String slot) {
        Optional<Element> element = getElementsInSlot(target, slot).findFirst();
        if (element.isPresent()) {
            return element.get().getComponent().get();
        }
        return null;
    }

    public static Component getPrefixComponent(Component target) {
        return getChildInSlot(target, "prefix");
    }
}
