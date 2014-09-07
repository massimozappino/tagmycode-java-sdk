package com.tagmycode.sdk;

import javax.xml.bind.DatatypeConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateParser {
    public Date parseDate(String dateString) throws ParseException {
        return DatatypeConverter.parseDateTime(dateString).getTime();
    }

    public String toISO8601(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date) + "Z";
    }
}
