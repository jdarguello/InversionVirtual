package com.BancoC.InversionVirtual.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Period;

@Converter(autoApply = true)
public class PeriodConverter implements AttributeConverter<Period, String> {
    
    @Override
    public String convertToDatabaseColumn(Period attribute) {
        return attribute != null ? attribute.toString() : null;
    }

    @Override
    public Period convertToEntityAttribute(String dbData) {
        return dbData != null ? Period.parse(dbData) : null;
    }
}
