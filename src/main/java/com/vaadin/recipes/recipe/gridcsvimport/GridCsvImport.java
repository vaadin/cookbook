package com.vaadin.recipes.recipe.gridcsvimport;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.util.SharedUtil;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Route("grid-csv-import")
@Metadata(
    howdoI = "Import/show CSV data in a grid",
    description = "Imports a CSV file with a header row and shows it in a Vaadin grid.",
    tags = { Tag.GRID, Tag.CSV },
    sourceFiles = { "input.csv" }
)
public class GridCsvImport extends Recipe {

    public GridCsvImport() {
        InputStreamReader csvFileReader = new InputStreamReader(
            getClass().getResourceAsStream("input.csv"),
            StandardCharsets.UTF_8
        );

        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader reader = new CSVReaderBuilder(csvFileReader).withCSVParser(parser).build();

        Grid<String[]> grid = new Grid<>();
        try {
            List<String[]> entries = reader.readAll();
            // Assume the first row contains headers
            String[] headers = entries.get(0);

            // Setup a grid with random data
            for (int i = 0; i < headers.length; i++) {
                final int columnIndex = i;
                String header = headers[i];
                String humanReadableHeader = SharedUtil.camelCaseToHumanFriendly(header);
                grid.addColumn(str -> str[columnIndex]).setHeader(humanReadableHeader);
            }
            grid.setItems(entries.subList(1, entries.size()));
            add(grid);
        } catch (IOException | CsvException e) {
            grid.addColumn(nop -> "Unable to load CSV: " + e.getMessage()).setHeader("Failed to import CSV file");
        }
    }
}
