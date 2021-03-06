package converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;
import java.time.LocalTime;

/**
 * Created by peter on 4/20/17.
 */
@Converter(autoApply = true)
public class LocalTimeAttributeConverter implements AttributeConverter<LocalTime, Time>{

    @Override
    public Time convertToDatabaseColumn(LocalTime attribute) {
        return (attribute == null) ? null : Time.valueOf(attribute);
    }

    @Override
    public LocalTime convertToEntityAttribute(Time dbData) {
        return (dbData == null) ? null : dbData.toLocalTime();
    }
}
