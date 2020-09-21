package com.vaadin.recipes.recipe.noticeclosedtab;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.SynchronizedRequestHandler;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

@Route("notice-closed")
@Metadata(
    howdoI = "Notice when a user closes their browser tab",
    description = "Use the Beacon API to get notified when a user closes a tab in a Vaadin application so you can run cleanup tasks."
)
public class NoticeClosed extends Recipe {

    public static class BeaconEvent extends ComponentEvent<UI> {

        public BeaconEvent(UI source, boolean fromClient) {
            super(source, fromClient);
        }
    }

    public static class BeaconHandler extends SynchronizedRequestHandler {
        private final UI ui;
        private final String beaconPath = "/beacon/" + UUID.randomUUID().toString();

        public BeaconHandler(UI ui) {
            this.ui = ui;
        }

        @Override
        protected boolean canHandleRequest(VaadinRequest request) {
            return beaconPath.equals(request.getPathInfo());
        }

        @Override
        public boolean synchronizedHandleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response)
            throws IOException {
            ComponentUtil.fireEvent(ui, new BeaconEvent(ui, true));
            return true;
        }

        public static Registration addBeaconListener(UI ui, ComponentEventListener<BeaconEvent> listener) {
            ensureInstalledForUi(ui);
            return ComponentUtil.addListener(ui, BeaconEvent.class, listener);
        }

        private static void ensureInstalledForUi(UI ui) {
            if (ComponentUtil.getData(ui, BeaconHandler.class) != null) {
                // Already installed, nothing to do
                return;
            }

            BeaconHandler beaconHandler = new BeaconHandler(ui);

            // ./beacon/<random uuid>
            String relativeBeaconPath = "." + beaconHandler.beaconPath;

            ui
                .getElement()
                .executeJs(
                    "window.addEventListener('unload', function() {navigator.sendBeacon && navigator.sendBeacon($0)})",
                    relativeBeaconPath
                );

            VaadinSession session = ui.getSession();
            session.addRequestHandler(beaconHandler);
            ui.addDetachListener(detachEvent -> session.removeRequestHandler(beaconHandler));

            ComponentUtil.setData(ui, BeaconHandler.class, beaconHandler);
        }
    }

    private final Div thisIdText = new Div();
    private final Div log = new Div();

    public NoticeClosed() {
        add(thisIdText, log);

        log.getStyle().set("white-space", "pre");

        refreshLog();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI ui = attachEvent.getUI();
        int uiId = ui.getUIId();

        thisIdText.setText("This UI has id " + uiId);

        addLogMessage("Attached " + uiId);

        Registration beaconRegistration = BeaconHandler.addBeaconListener(
            ui,
            beaconEvent -> {
                addLogMessage("Browser close event for " + uiId);
            }
        );

        // Polling only needed for the demo
        ui.setPollInterval(1000);
        Registration pollRegistration = ui.addPollListener(
            pollEvent -> {
                refreshLog();
            }
        );

        addDetachListener(
            detachEvent -> {
                detachEvent.unregisterListener();
                beaconRegistration.remove();

                // Polling only needed for the demo
                ui.setPollInterval(-1);
                pollRegistration.remove();
            }
        );
    }

    private void addLogMessage(String message) {
        VaadinSession.getCurrent().setAttribute("log", getLogValue() + "\n" + LocalTime.now() + " " + message);

        refreshLog();
    }

    private void refreshLog() {
        log.setText(getLogValue());
    }

    private static String getLogValue() {
        return Objects.toString(VaadinSession.getCurrent().getAttribute("log"), "");
    }
}
