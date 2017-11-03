package com.tagmycode.sdk;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateParserTest {

    @Test
    public void parseDate() throws ParseException {
        Date date = DateParser.parseDate("2010-11-22T01:11:25+02:00");
        assertEquals(1290381085000L, date.getTime());
    }

    @Test
    public void dateToJson() throws ParseException {
        Date date = DateParser.parseDate("2010-11-22T01:11:25+02:00");
        assertEquals("2010-11-21T23:11:25Z", new DateParser(date).toISO8601());

        date = DateParser.parseDate("2010-11-22T01:11:25Z");
        assertEquals("2010-11-22T01:11:25Z", new DateParser(date).toISO8601());
    }

    @Test
    public void toDateLocale() throws ParseException {
        Date date = DateParser.parseDate("2010-11-22T01:11:25+02:00");
        DateParser dateParser = new DateParser(date);
        dateParser.setTimezone(TimeZone.getTimeZone("gmt"));
        assertEquals("Sunday, November 21, 2010", dateParser.toDateLocale(DateFormat.FULL, Locale.US));
    }

    @Test
    public void toTimeLocale() throws ParseException {
        Date date = DateParser.parseDate("2010-11-22T01:11:25+02:00");
        DateParser dateParser = new DateParser(date);
        dateParser.setTimezone(TimeZone.getTimeZone("gmt"));
        assertEquals("11:11:25 PM GMT", dateParser.toTimeLocale(DateFormat.FULL, Locale.US));
    }

    @Test
    public void toDateTimeLocale() throws ParseException {
        Date date = DateParser.parseDate("2010-11-22T01:11:25+02:00");
        DateParser dateParser = new DateParser(date);
        dateParser.setTimezone(TimeZone.getTimeZone("gmt"));
        assertEquals("Nov 21, 2010 11:11:25 PM", dateParser.toDateTimeLocale(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.US));
    }

}