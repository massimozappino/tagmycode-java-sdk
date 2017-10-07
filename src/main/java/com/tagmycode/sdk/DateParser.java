package com.tagmycode.sdk;

import javax.xml.bind.DatatypeConverter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateParser {

    private Date date;

    public DateParser(Date date) {
        this.date = date;
    }

    public static Date parseDate(String dateString) throws ParseException {
        return DatatypeConverter.parseDateTime(dateString).getTime();
    }

    public String toISO8601() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date) + "Z";
    }

    public String toDateLocale(int style, Locale locale) {
        DateFormat dateInstance = DateFormat.getDateInstance(style, locale);
        return dateInstance.format(date);
    }

    public String toTimeLocale(int style, Locale locale) {
        DateFormat timeInstance = DateFormat.getTimeInstance(style, locale);
        return timeInstance.format(date);
    }

    public String toDateTimeLocale(int dateStyle, int timeStyle, Locale locale) {
        return String.format("%s %s", toDateLocale(dateStyle, locale),
                toTimeLocale(timeStyle, locale));
    }
}
