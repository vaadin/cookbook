package com.vaadin.recipes.recipe.userlocalegridnumbers;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class ApplicationI18NProvider implements I18NProvider {

    @Override
    public List<Locale> getProvidedLocales() {
        // Claim that the application support anything that Java supports
        return Arrays.asList(Locale.getAvailableLocales());
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        // Not used in this example
        throw new UnsupportedOperationException();
    }
}
