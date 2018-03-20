package com.dlegeza.stocks.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component
public class DateTimeSerializer extends JsonSerializer<Timestamp> {
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void serialize(Timestamp date, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        String formattedDate = DATE_TIME_FORMAT.format(date);
        gen.writeString(formattedDate);
    }
}