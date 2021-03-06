package com.tagmycode.sdk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateParser {

    private TimeZone timezone;
    private final Date date;

    public DateParser(Date date) {
        this.date = date;
        this.timezone = TimeZone.getDefault();
    }

    public static Date parseDate(String dateString) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(zonedDateTime.toInstant(), ZoneOffset.UTC);
        return Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());
    }

    public String toISO8601() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

    public String toDateLocale(int style, Locale locale) {
        DateFormat dateInstance = DateFormat.getDateInstance(style, locale);
        dateInstance.setTimeZone(timezone);
        return dateInstance.format(date);
    }

    public String toTimeLocale(int style, Locale locale) {
        DateFormat timeInstance = DateFormat.getTimeInstance(style, locale);
        timeInstance.setTimeZone(timezone);
        return timeInstance.format(date);
    }

    public String toDateTimeLocale(int dateStyle, int timeStyle, Locale locale) {
        return String.format("%s %s", toDateLocale(dateStyle, locale),
                toTimeLocale(timeStyle, locale));
    }

    public void setTimezone(TimeZone timezone) {
        this.timezone = timezone;
    }
}
