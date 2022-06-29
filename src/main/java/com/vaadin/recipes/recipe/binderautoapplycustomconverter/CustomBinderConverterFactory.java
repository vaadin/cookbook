package com.vaadin.recipes.recipe.binderautoapplycustomconverter;

import java.time.Duration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("custom-binder-converter-factory")
@Metadata(howdoI = "Automatically apply custom converters with Binder",
        description = "When using Binder.bindInstanceFields() Vaadin automatically apply converters when model and presentation data types do not match. This recipe shows how to create a Binder instance that also supports custom converters.",
        sourceFiles = "CustomBinder.java",
        tags = Tag.BINDER)
public class CustomBinderConverterFactory extends Recipe {

    private final TextField time;

    public CustomBinderConverterFactory() {
        time = new TextField("Time");
        time.setPlaceholder("1h 30m 10s");

        // Create instance of the custom binder and bind instance fields
        // custom converter will be automatically applied for time field
        Binder<Data> binder = new CustomBinder<>(Data.class);
        binder.bindInstanceFields(this);

        Data result = new Data();
        binder.setBean(result);

        Button saveButton = new Button("Add", ev -> {
            if (binder.validate().isOk()) {
                add(new Span(result.toString()));
            }
        });

        HorizontalLayout form = new HorizontalLayout();
        form.setDefaultVerticalComponentAlignment(Alignment.END);
        add(time, new Span("Time syntax: Nh Nm Ns (e.g. 1h 23m 10s or 97m)"), saveButton, new Hr());
    }

    public static class Data {
        private Duration time;

        public Duration getTime() {
            return time;
        }

        public void setTime(Duration time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return time.toString();
        }
    }
}
