package com.vaadin.recipes.recipe.hostname;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Route("hostname")
@Metadata(howdoI = "Get the server hostname", description = "This recipe demonstrates various ways of getting the server hostname.", tags = { Tag.FLOW })
public class Hostname extends Recipe {

    public Hostname() {
        add(new H3("VaadinServletRequest"));
        displayHostnameUsingVaadinServletRequest();

        add(new H3("HTTP Header"));
        displayHostnameUsingHTTPHeader();

        add(new H3("InetAddress"));
        displayHostnameUsingInetAddress();

        add(new H3("Environment Variable"));
        displayHostnameUsingEnvironmentVariable();

        add(new H3("Shell Command"));
        displayHostnameUsingShellCommand();

        add(new H3("ActiveViewLocation"));
        displayHostnameUsingActiveViewLocation();

        add(new H3("JavaScript"));
        displayHostnameUsingJavaScript();
    }

    private void displayHostnameUsingVaadinServletRequest() {
        try {
            HttpServletRequest httpServletRequest = ((VaadinServletRequest)VaadinRequest.getCurrent()).getHttpServletRequest();
            String hostname = httpServletRequest.getServerName();
            displayNotification(hostname);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void displayHostnameUsingHTTPHeader() {
        try {
            String hostname = VaadinRequest.getCurrent().getHeader("host");
            displayNotification(hostname);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void displayHostnameUsingInetAddress() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostname = addr.getHostName();
            displayNotification(hostname);
        } catch (UnknownHostException e) {
            handleException(e);
        }
    }

    private void displayHostnameUsingEnvironmentVariable() {
        try {
            String hostname = System.getenv("HOSTNAME"); // Unix-like
            if (hostname == null) {
                hostname = System.getenv("COMPUTERNAME"); // Windows
            }
            displayNotification(hostname);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void displayHostnameUsingShellCommand() {
        try {
            String hostname = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("hostname").getInputStream())).readLine().trim();
            displayNotification(hostname);
        } catch (IOException e) {
            handleException(e);
        }
    }

    private void displayHostnameUsingActiveViewLocation() {
        try {
            String hostname = UI.getCurrent().getInternals().getLastHandledLocation().getPath();
            displayNotification(hostname);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void displayHostnameUsingJavaScript() {
        try {
            Page page = UI.getCurrent().getPage();
            page.executeJs("return document.location.host;").then(r -> {
                displayNotification(r.asString());
            });
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void displayNotification(String message) {
        add(new Span(message));
    }

    private void handleException(Exception e) {
        Notification.show("An error occurred: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
    }
}
