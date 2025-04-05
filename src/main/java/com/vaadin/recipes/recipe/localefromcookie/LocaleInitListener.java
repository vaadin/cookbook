package com.vaadin.recipes.recipe.localefromcookie;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServiceInitListener;

import jakarta.servlet.http.Cookie;

/*
 * In a regular application, this could be simpler as a @Bean method in Application.java 
 */
@Component
public class LocaleInitListener implements VaadinServiceInitListener {
    static final String COOKIE_NAME = "my-app-locale";

    @Override
    public void serviceInit(ServiceInitEvent serviceInit) {
        serviceInit.getSource().addSessionInitListener(sessionInit -> {
            findCookie(sessionInit.getRequest()).ifPresent(cookie -> {
                Locale locale = new Locale(cookie.getValue());
                sessionInit.getSession().setLocale(locale);
            });
        });
    }

    static Optional<Cookie> findCookie(VaadinRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Stream.of(request.getCookies()).filter(cookie -> cookie.getName()
                .equals(LocaleInitListener.COOKIE_NAME)).findAny();
    }
}
