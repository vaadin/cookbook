package com.vaadin.recipes.recipe.dynamicgridrowbackgroundcolor;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.time.Month;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("dynamic-grid-row-background-color")
@Metadata(
    howdoI = "Set grid row background color dynamically",
    description = "Learn how to change the row background color of a Vaadin grid, based on any dynamic data.",
    sourceFiles = { "recipe/dynamicgridrowbackgroundcolor/dynamic-grid-row-background-color.css" },
    tags = { Tag.THEME, Tag.GRID }
)
@CssImport(
    themeFor = "vaadin-grid",
    value = "./recipe/dynamicgridrowbackgroundcolor/dynamic-grid-row-background-color.css"
)
public class DynamicGridRowBackgroundColor extends Recipe {
    private Random r = new Random();

    public DynamicGridRowBackgroundColor() {
        Grid<MonthlyExpense> expensesGrid = new Grid<>(MonthlyExpense.class);
        expensesGrid.setColumns("month", "expense");
        expensesGrid.setItems(Stream.of(Month.values()).map(this::generateMonthlyExpense).collect(Collectors.toList()));
        expensesGrid.setAllRowsVisible(true);
        add(expensesGrid);

        expensesGrid.setPartNameGenerator(monthlyExpense -> monthlyExpense.getExpense() > 500 ? "warn" : null);
    }

    private MonthlyExpense generateMonthlyExpense(Month month) {
        return new MonthlyExpense(month.toString(), 100 + r.nextInt(1000 - 100));
    }

    public static class MonthlyExpense {
        private String month;
        private int expense;

        public MonthlyExpense(String month, int expense) {
            this.month = month;
            this.expense = expense;
        }

        public String getMonth() {
            return this.month;
        }

        public int getExpense() {
            return this.expense;
        }
    }
}
