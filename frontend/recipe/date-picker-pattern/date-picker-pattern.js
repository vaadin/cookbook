import { format, parse } from 'date-fns';

// @ts-ignore
window._setDatePickerPattern = (datePicker, pattern) => {
  datePicker.set('i18n.formatDate', ({ year, month, day }) => {
    return format(new Date(year, month, day), pattern)
  });

  datePicker.set('i18n.parseDate', (dateString) => {
    const parsed = parse(dateString, pattern, new Date());
    return {
      day: parsed.getDate(),
      month: parsed.getMonth(),
      year: parsed.getFullYear(),
    };
  });
};
