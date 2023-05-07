package com.example.demo.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

// TODO: Make changes in file schema to save dates in date format
// for now, we are saving dates as string
// this class will be used to convert string to date
public class StringToDateConverter implements Converter<String, Date> {

    private String dateFormatPattern;

    public StringToDateConverter(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    @Override
    public Date convert(String source) {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFormatPattern);
            return formatter.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }
}
