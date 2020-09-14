package com.vaadin.recipes.recipe.livedata;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

import reactor.core.Disposable;

@Route("show-real-time-updating-data")
@JsModule("@vaadin/vaadin-charts/theme/vaadin-chart-default-theme")
@Metadata(
    howdoI = "Show real-time updating content",
    description = "Learn how to automatically update the UI with data from a reactive backend.",
    tags = { Tag.PUSH },
    sourceFiles = { "StockDataService.java" }
)
public class LiveData extends Recipe {

    private String ticker = "FOO";
    private Span currentPrice = new Span();
    private ListSeries series = new ListSeries(ticker);

    private StockDataService service;
    private Disposable subscription;

    LiveData(StockDataService service) {
        this.service = service;
        // Setup UI components
        var header = new H2(ticker + " – ");
        var chart = new Chart(ChartType.LINE);
        chart.getConfiguration().getChart().setStyledMode(true);

        header.add(currentPrice);
        var info = new Paragraph("The service in this demo sends 30 data points. ");
        var link = new Anchor(
            "/client-side-view-displaying-live-data",
            "See this example using the client-side TypeScript API."
        );
        info.add(link);
        add(header, info, chart);

        chart.getConfiguration().addSeries(series);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        UI ui = attachEvent.getUI();

        // Hook up to service for live updates
        subscription = service.getStockPrice(ticker).subscribe(price -> {
            ui.access(() -> {
                currentPrice.setText("$" + price);
                series.addData(price);
            });
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cancel subscription when the view is detached
        subscription.dispose();

        super.onDetach(detachEvent);
    }
}
