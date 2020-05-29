package org.vaadin.recipes;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
@PWA(name = "Vaadin Recipes", shortName = "vaadin--recipes", enableInstallPrompt = false)
public class AppShell implements AppShellConfigurator {
}
