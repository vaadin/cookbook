package com.vaadin.recipes.recipe.dynamicgridcellstyling;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("dynamic-grid-cell-styling")
@Metadata(
    howdoI = "Style grid cell contents dynamically",
    description = "Learn how to style the contents of a given Vaadin grid cell.",
    sourceFiles = { "recipe/dynamicgridcellstyling/dynamic-grid-cell-styling.css" },
    tags = { Tag.THEME, Tag.GRID }
)
@CssImport(themeFor = "vaadin-grid", value = "./recipe/dynamicgridcellstyling/dynamic-grid-cell-styling.css")
public class DynamicGridCellStyling extends Recipe {

    private Random r = new Random();

    public DynamicGridCellStyling() {
        Grid<MonthlyExpense> grid = new Grid<>(MonthlyExpense.class, false);
        grid
            .addColumn(
                LitRenderer
                    .<MonthlyExpense>of(
                        "Month: <span class='month ${item.season}'>${item.month}</span><br>Expense: <span style=${item.expense > 500?'color: red':''}>${item.expense}</span>"
                    )
                    .withProperty("month", item -> item.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                    .withProperty("season", item -> getSeason(item.getMonth()))
                    .withProperty("expense", MonthlyExpense::getExpense)
            )
            .setHeader("Styled inside cell");

        grid
            .addColumn(
                LitRenderer
                    .<MonthlyExpense>of("Expense: <span part='expense'>${item.expense}</span>")
                    .withProperty("expense", MonthlyExpense::getExpense)
            )
            .setHeader("Styled using generator (can only target the whole cell)")
            .setClassNameGenerator(item -> item.getExpense() > 500 ? "toomuch" : "");
        grid.setItems(Stream.of(Month.values()).map(this::generateMonthlyExpense).collect(Collectors.toList()));
        grid.setAllRowsVisible(true);
        add(grid);
    }

    private String getSeason(Month month) {
        if (month == Month.DECEMBER || month == Month.JANUARY || month == Month.FEBRUARY) {
            return "winter";
        } else if (month == Month.MARCH || month == Month.APRIL || month == Month.MAY) {
            return "spring";
        } else if (month == Month.JUNE || month == Month.JULY || month == Month.AUGUST) {
            return "summer";
        } else {
            return "fall";
        }
    }

    private MonthlyExpense generateMonthlyExpense(Month month) {
        return new MonthlyExpense(month, 100 + r.nextInt(1000 - 100));
    }

    public static class MonthlyExpense {

        private Month month;
        private int expense;

        public MonthlyExpense(Month month, int expense) {
            this.month = month;
            this.expense = expense;
        }

        public Month getMonth() {
            return this.month;
        }

        public int getExpense() {
            return this.expense;
        }
    }
}
