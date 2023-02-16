package com.vaadin.recipes.recipe.griddetailstable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import com.vaadin.recipes.recipe.griddetailstable.MonthlyExpense.DailyExpenses;

@Route("grid-details-table")
@Metadata(howdoI = "Generate a html table in Grid details", description = "Grid itself is a heavy component and might be overkill to use in Grid details. Here we generate a light weight html table with TemplateRenderer to show details data of another bean type. This will keep the complexity of the view in control.", sourceFiles = {
        "MonthlyExpense.java" }, tags = { Tag.GRID })
public class GridDetailsTable extends Recipe {
    private Grid<MonthlyExpense> grid = new Grid<>(MonthlyExpense.class);
    private static String TD = "<td style=\"border: 1px solid lightgrey; width: 33.3%; padding: 3px;\">";
    private static String TABLEHEADER = "<tr>" + TD + "<b>Day</b></td>" + TD
            + "<b>Explanation</b></td>" + TD + "<b>Amount</b></td></tr>";

    public GridDetailsTable() {
        setHeight("500px");
        grid.setItems(getData());
        grid.setColumns("year", "month", "expenses");
        grid.setItemDetailsRenderer(LitRenderer.<MonthlyExpense> of(
                "<div style=\"border: 1px solid gray; width: 100%;\" .innerHTML=\"${item.html}\"></div>")
                .withProperty("html",
                        // Generate Table for the DailyExpenses of the selected
                        // month
                        expense -> getTable(expense.getDailyExpenses())));
        grid.setSizeFull();
        add(grid);
    }

    private static List<MonthlyExpense> getData() {
        String[] monthNames = new java.text.DateFormatSymbols().getMonths();
        List<MonthlyExpense> data = new ArrayList<>();
        for (int year = 2000; year < 2020; year++) {
            for (int month = 0; month < 12; month++) {
                data.add(new MonthlyExpense(monthNames[month], year));
            }
        }
        return data;
    }

    private static String getTable(List<DailyExpenses> dailyExpenses) {
        String table = "<table style=\"width: 100%\">" + TABLEHEADER
        // Collect stream of DailyExpenses to a String for each row of the Table
                + dailyExpenses.stream()
                        .map(dailyExpense -> "<tr>" + TD + dailyExpense.getDay()
                                + "</td>" + TD + dailyExpense.getExplanation()
                                + "</td>" + TD + dailyExpense.getAmount()
                                + "</td>")
                        .collect(Collectors.joining("</tr>"))
                + "</table>";
        return table;
    }
}
