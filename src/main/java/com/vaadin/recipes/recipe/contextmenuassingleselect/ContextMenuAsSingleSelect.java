package com.vaadin.recipes.recipe.contextmenuassingleselect;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import java.util.List;
import java.util.StringJoiner;

@Route("contextmenu-as-single-select")
@Metadata(
        howdoI = "Use a contextmenu as bindable single-select-field",
        description = "Implement AbstractCompositeField and wrap a ContextMenu-Component to use it as a kind of single-select like ListBox, Select etc."
)
public class ContextMenuAsSingleSelect extends Recipe {


    public ContextMenuAsSingleSelect() {

        Button targetButton = new Button("select person");

        SingleSelectContextMenu<Person> singleSelectContextMenu = new SingleSelectContextMenu<>(null);
        singleSelectContextMenu.setTarget(targetButton);

        this.add(targetButton);

        singleSelectContextMenu.setItems(new Person("Olaf"), new Person("John"), new Person("Jim"));

        singleSelectContextMenu.setRenderer(item -> {
            return new Span(VaadinIcon.USER.create(), new Span(item.getName()));
        });

        AnyBindableBean bean = new AnyBindableBean();
        Binder<AnyBindableBean> binder = new Binder<AnyBindableBean>();

        binder.forField(singleSelectContextMenu)
                .bind(AnyBindableBean::getPerson, AnyBindableBean::setPerson);

        binder.setBean(bean);

        binder.addValueChangeListener(valueChangeEvent -> {
            Notification.show("Value: " + valueChangeEvent.getValue().toString());
        });

    }

    public static class Person {

        private final String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .toString();
        }
    }

    public static class AnyBindableBean {

        private Person person;

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }
    }
}
