package com.vaadin.recipes.recipe.localefromcookie;

import java.time.Duration;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;

import jakarta.servlet.http.Cookie;

@Route(LocaleFromCookie.ROUTE)
@Metadata(howdoI = "Set the UI locale based on a cookie", description = "When a new session is created, the locale is set based on a cookie if present.", sourceFiles = {
        "LocaleInitListener.java" })
public class LocaleFromCookie extends Recipe {

    static final String ROUTE = "locale-from-cookie";

    public LocaleFromCookie() {
        add("Session locale: " + getLocale());

        VaadinRequest request = VaadinRequest.getCurrent();

        TextField cookieField = new TextField("Cookie value");
        LocaleInitListener.findCookie(request)
                .ifPresent(cookie -> cookieField.setValue(cookie.getValue()));

        Button setCookie = new Button("Set cookie", click -> {
            String value = cookieField.getValue();
            Cookie cookie = new Cookie(LocaleInitListener.COOKIE_NAME, value);
            if (value.isEmpty()) {
                // Deletes the cookie
                cookie.setMaxAge(0);
                add("Cookie deleted");
            } else {
                cookie.setMaxAge((int) Duration.ofDays(365).toSeconds());
                add("Cookie set to '" + value + "'.");
            }
            VaadinResponse.getCurrent().addCookie(cookie);

            Anchor anchor = new Anchor(ROUTE + "?restartApplication",
                    "Reload with a new session to see the impact of the cookie");
            anchor.setRouterIgnore(true);
            add(anchor);
        });

        HorizontalLayout layout = new HorizontalLayout(cookieField, setCookie);
        layout.setAlignItems(Alignment.BASELINE);
        add(layout);
    }

}
