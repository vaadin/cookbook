package com.vaadin.recipes.recipe.livedata;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;

@Route("show-real-time-updating-data")
@Metadata(
    howdoI = "Show real-time updating content",
    description = "Learn how to automatically update the UI with data from a reactive backend.",
    tags = { Tag.PUSH },
    sourceFiles = { "StockDataService.java" }
)
public class LiveData extends Recipe {

    LiveData(StockDataService service) {
        // Setup UI components
        var ticker = "FOO";
        var header = new H2(ticker + " – ");
        var currentPrice = new Span();
        var chart = new Chart(ChartType.LINE);
        header.add(currentPrice);
        var info = new Paragraph("The service in this demo sends 30 data points. ");
        var link = new Anchor(
            "/client-side-view-displaying-live-data",
            "See this example using the client-side TypeScript API."
        );
        info.add(link);
        add(header, info, chart);

        // Configure chart
        var series = new ListSeries(ticker);
        chart.getConfiguration().addSeries(series);

        // Hook up to service for live updates
        service
            .getStockPrice(ticker)
            .subscribe(
                price -> {
                    getUI()
                        .ifPresent(
                            ui ->
                                ui.access(
                                    () -> {
                                        currentPrice.setText("$" + price);
                                        series.addData(price);
                                    }
                                )
                        );
                }
            );
    }
}
