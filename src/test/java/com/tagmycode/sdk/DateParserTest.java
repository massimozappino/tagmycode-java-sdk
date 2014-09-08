package com.tagmycode.sdk;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateParserTest {

    @Test
    public void parseDate() throws ParseException {
        Date date = new DateParser().parseDate("2010-11-22T01:11:25+02:00");
        assertEquals(1290381085000L, date.getTime());
    }

    @Test
    public void dateToJson() throws ParseException {
        Date date = new DateParser().parseDate("2010-11-22T01:11:25+02:00");
        assertEquals("2010-11-21T23:11:25Z", new DateParser().toISO8601(date));

        date = new DateParser().parseDate("2010-11-22T01:11:25Z");
        assertEquals("2010-11-22T01:11:25Z", new DateParser().toISO8601(date));
    }
}