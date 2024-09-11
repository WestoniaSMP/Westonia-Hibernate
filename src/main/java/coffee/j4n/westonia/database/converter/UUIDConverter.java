package coffee.j4n.westonia.database.converter;

import jakarta.persistence.AttributeConverter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * AttributeConverter for UUIDs used by Hibernate.
 */
public class UUIDConverter implements AttributeConverter<UUID, String> {

    /**
     * Converts the given UUID to a String.
     *
     * @param uuidToConvert The UUID to convert.
     * @return The UUID as a String.
     */
    public String convertToDatabaseColumn(final @NotNull UUID uuidToConvert) {
        return uuidToConvert.toString();
    }

    /**
     * Converts the given String to a UUID.
     *
     * @param stringToConvert The String to convert.
     * @return The String as a UUID.
     */
    public UUID convertToEntityAttribute(final @NotNull String stringToConvert) {
        return UUID.fromString(stringToConvert);
    }
}