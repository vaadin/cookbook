package com.vaadin.recipes.recipe.griddetailstable;

import java.util.ArrayList;
import java.util.List;

public class MonthlyExpense {

    private String month;
    private Double expenses;
    private int year;
    private List<DailyExpenses> dailyExpenses;

    public MonthlyExpense(String month, int year) {
        setMonth(month);
        setExpenses(expenses);
        setYear(year);
        List<DailyExpenses> list = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            list.add(new DailyExpenses(i));
        }
        setDailyExpenses(list);
        Double sum = list.stream().map(entry -> entry.getAmount()).reduce(0d, Double::sum);
        setExpenses(sum);
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getExpenses() {
        return expenses;
    }

    public void setExpenses(Double expenses) {
        this.expenses = expenses;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private void setDailyExpenses(List<DailyExpenses> list) {
        dailyExpenses = list;
    }

    public List<DailyExpenses> getDailyExpenses() {
        return dailyExpenses;
    }

    public class DailyExpenses {

        private String explanation = "Explanation of the expense";
        private Double amount = 0d;
        private Integer day;

        public DailyExpenses(Integer day) {
            this.day = day;
            this.amount = Math.floor((Math.random() * 1000) % 500 + 300);
        }

        public String getExplanation() {
            return explanation;
        }

        public Double getAmount() {
            return amount;
        }

        public Integer getDay() {
            return day;
        }
    }
}
