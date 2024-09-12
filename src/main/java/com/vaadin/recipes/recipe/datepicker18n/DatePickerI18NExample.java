package com.vaadin.recipes.recipe.datepicker18n;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.recipes.recipe.Metadata;
import com.vaadin.recipes.recipe.Recipe;
import com.vaadin.recipes.recipe.Tag;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Route("date-picker-i18n")
@Metadata(howdoI = "Configure DatePicker I18N from Locale",
    description = "Initialize DatePicker I18N from translations provided by Locale.",
    tags = {Tag.JAVA, Tag.FLOW})
public class DatePickerI18NExample extends Recipe {

  // Note, you can find a Date/DateTimePicker components working
  // this way out of the box from Viritin and SuperFields add-on.
  // https://vaadin.com/directory/flow-viritin
  // https://vaadin.com/directory/component/superfields

  enum YearFormat {
    DEFAULT, LONG, SHORT
  }

  private static DatePickerI18n getDatePickerI18Ninstance(Locale locale, YearFormat yearFormat) {
    DatePickerI18n i18n = new DatePickerI18n();
    DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);

    i18n.setMonthNames(List.of(dfs.getMonths()));
    i18n.setWeekdays(List.of(dfs.getWeekdays()).stream().skip(1).toList());
    i18n.setWeekdaysShort(List.of(dfs.getShortWeekdays()).stream().skip(1).toList());
    i18n.setDateFormat(getPattern(locale, yearFormat));

    DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
    i18n.setFirstDayOfWeek(firstDayOfWeek.getValue() % 7);
    return i18n;
  }

  private static String getPattern(Locale locale, YearFormat yearFormat) {
    String pattern =
        ((SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale)).toPattern();
    switch (yearFormat) {
      case LONG:
        pattern = pattern.replaceFirst("y+", "yyyy");
        break;
      case SHORT:
        pattern = pattern.replaceFirst("y+", "yy");
        break;
      case DEFAULT:
        break;
    }
    return pattern;
  }

  DatePicker datePicker;

  public DatePickerI18NExample() {
    datePicker = new DatePicker("Date Picker", LocalDate.now());

    selectYearFormat.setItems(YearFormat.values());
    selectYearFormat.setValue(YearFormat.DEFAULT);

    comboLanguage.setItems(Stream.of(Locale.getAvailableLocales()).map(Locale::getLanguage)
        .distinct().map(Locale::new).filter(locale -> !locale.getDisplayLanguage().isEmpty())
        .sorted(Comparator.comparing(Locale::getDisplayLanguage)).toArray(Locale[]::new));

    comboLanguage.setItemLabelGenerator(Locale::getDisplayLanguage);

    comboCountry.setEnabled(false);

    comboLanguage.addValueChangeListener(ev -> onLanguageChange(ev.getValue()));
    comboLanguage.addValueChangeListener(ev -> onLocaleChange(ev.getValue()));

    selectYearFormat.addValueChangeListener(ev -> onLocaleChange(comboCountry.getValue()));

    comboCountry.setItemLabelGenerator(Locale::getDisplayCountry);

    textPattern.setReadOnly(true);
    textFirstDayOfWeek.setReadOnly(true);

    comboCountry.addValueChangeListener(ev -> {

    });

    add(datePicker, comboLanguage, comboCountry, selectYearFormat, textPattern, textFirstDayOfWeek);
  }

  private void onLocaleChange(Locale locale) {
    YearFormat yearFormat = selectYearFormat.getValue();
    if (locale != null) {
      datePicker.setI18n(getDatePickerI18Ninstance(locale, yearFormat));
      DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
      textPattern.setValue(getPattern(locale, yearFormat));
      textFirstDayOfWeek.setValue(firstDayOfWeek.toString());
    }
  }

  private void onLanguageChange(Locale newLocale) {
    if (newLocale == null) {
      comboCountry.clear();
      comboCountry.setEnabled(false);
      textPattern.clear();
      textFirstDayOfWeek.clear();
    } else {
      String language = newLocale.getLanguage();
      Locale items[] = Stream.of(Locale.getAvailableLocales())
          .filter(locale -> locale.getLanguage().equals(language))
          .filter(locale -> !locale.getDisplayCountry().isEmpty())
          .sorted(Comparator.comparing(Locale::getDisplayCountry)).toArray(Locale[]::new);
      comboCountry.setItems(items);

      comboCountry.setEnabled(items.length > 1);
      if (items.length == 1) {
        comboCountry.setValue(items[0]);
      } else {
        comboCountry.getElement().executeJs("this.open()");
      }
      onLocaleChange(newLocale);
    }
  }

  private ComboBox<Locale> comboLanguage = new ComboBox<>("Language");
  private ComboBox<Locale> comboCountry = new ComboBox<>("Country");
  private TextField textPattern = new TextField("Pattern");
  private TextField textFirstDayOfWeek = new TextField("First day of week");
  private Select<YearFormat> selectYearFormat = new Select<>();

}
