package com.tagmycode.sdk;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

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
        assertEquals("Monday, November 22, 2010", new DateParser(date).toDateLocale(DateFormat.FULL, Locale.US));
    }

    @Test
    public void toTimeLocale() throws ParseException {
        Date date = DateParser.parseDate("2010-11-22T01:11:25+02:00");
        assertEquals("12:11:25 AM CET", new DateParser(date).toTimeLocale(DateFormat.FULL, Locale.US));
    }

    @Test
    public void toDateTimeLocale() throws ParseException {
        Date date = DateParser.parseDate("2010-11-22T01:11:25+02:00");
        assertEquals("Nov 22, 2010 12:11:25 AM", new DateParser(date).toDateTimeLocale(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.US));
    }

}