package com.vaadin.recipes.recipe.formtabs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatusHandler;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("form-tabs")
@Metadata(howdoI = "Divide a form into tabs", description = "A form that is divided into tabs so that each tab shows the validation status of the fields in that tab.", tags = {
        Tag.JAVA, Tag.FORM })
public class FormTabs extends Recipe {
    public static class Person {
        private String name;
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    private Binder<Person> binder = new Binder<>(Person.class);

    private TextField name = new TextField("Name");
    private TextField address = new TextField("Address");

    private Tabs tabs = new Tabs();
    private Component currentContent = new Div();

    private Map<Tab, Component> parts = new LinkedHashMap<>();
    private Map<Component, Tab> rootToTab = new HashMap<>();

    private Set<HasValue<?, ?>> fieldsWithErrors = new HashSet<>();

    private final Button saveButton;

    public FormTabs() {
        binder.forMemberField(name).asRequired();
        binder.forMemberField(address).asRequired();
        binder.bindInstanceFields(this);

        parts.put(new Tab("Part 1"), new VerticalLayout(name));
        parts.put(new Tab("Part 2"), new VerticalLayout(address));

        parts.forEach((tab, root) -> rootToTab.put(root, tab));
        parts.keySet().forEach(tabs::add);

        saveButton = new Button("Save", event -> submit());

        add(tabs, currentContent, new VerticalLayout(saveButton));

        tabs.addSelectedChangeListener(event -> showTab(event.getSelectedTab()));
        showTab(tabs.getSelectedTab());

        BinderValidationStatusHandler<Person> defaultValidationHandler = binder.getValidationStatusHandler();
        binder.setValidationStatusHandler(statusChange -> {
            defaultValidationHandler.statusChange(statusChange);

            statusChange.getFieldValidationStatuses().forEach(status -> {
                if (status.isError()) {
                    fieldsWithErrors.add(status.getField());
                } else {
                    fieldsWithErrors.remove(status.getField());
                }
            });

            Set<Tab> partsWithErrors = fieldsWithErrors.stream().map(field -> findTabContaining((Component) field))
                    .collect(Collectors.toSet());

            parts.keySet().forEach(tab -> setTabErrorIcon(tab, partsWithErrors.contains(tab)));
        });
    }

    private static void setTabErrorIcon(Tab tab, boolean partHasErrors) {
        Component currentIcon = tab.getChildren().filter(child -> child instanceof Icon).findAny().orElse(null);

        if (partHasErrors && currentIcon == null) {
            tab.add(VaadinIcon.EXCLAMATION.create());
        } else if (!partHasErrors && currentIcon != null) {
            tab.remove(currentIcon);
        }
    }

    private void showTab(Tab tab) {
        Component newContent = parts.get(tab);
        replace(currentContent, newContent);
        currentContent = newContent;
    }

    private void submit() {
        Person bean = new Person();
        try {
            binder.writeBean(bean);
            Notification.show("Saved person: " + bean.getName() + ", " + bean.getAddress());
        } catch (ValidationException e) {
            List<BindingValidationStatus<?>> validationErrors = e.getFieldValidationErrors();
            if (validationErrors.isEmpty()) {
                Notification.show("Could not save. ¯\\_(ツ)_/¯");
            } else {
                HasValue<?, ?> firstFieldWithError = validationErrors.get(0).getField();
                Tab firstTabWithError = findTabContaining((Component) firstFieldWithError);
                tabs.setSelectedTab(firstTabWithError);
            }
        }
    }

    private Tab findTabContaining(Component component) {
        Component root = findRoot(rootToTab.keySet(), component);

        return rootToTab.get(root);
    }

    private Component findRoot(Set<Component> candidates, Component component) {
        if (candidates.contains(component)) {
            return component;
        } else {
            return findRoot(candidates, component.getParent()
                    .orElseThrow(() -> new IllegalStateException("Component is not a descendant of any candidate")));
        }
    }
}
