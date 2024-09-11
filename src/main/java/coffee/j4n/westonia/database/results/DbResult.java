package coffee.j4n.westonia.database.results;

import coffee.j4n.westonia.utils.statics.enums.ResultType;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Represents the result of a database operation.
 *
 * @param <T> The type of the resulting object.
 */
public class DbResult<T> {

    /**
     * The result of the operation (object or null).
     */
    private final T result;

    /**
     * The message of the result (e.g. an error message).
     */
    private final String message;

    /**
     * The type of the result.
     */
    private final ResultType resultType;

    /**
     * Creates a new instance of the DbResult
     *
     * @param result The result of the operation.
     * @param message The message of the result.
     * @param resultType The type of the result.
     */
    public DbResult(T result, String message, ResultType resultType) {
        this.result = result;
        this.message = message;
        this.resultType = resultType;
    }

    /**
     * Gets the result out of the result.
     *
     * @return The result of the operation.
     */
    public @Nullable T getResult() {
        return result;
    }

    /**
     * Gets the message of the result.
     *
     * @return The message of the result.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the type of the result.
     *
     * @return The type of the result.
     */
    public ResultType getResultType() {
        return resultType;
    }

    /**
     * Checks if the operation was successful.
     *
     * @return Whether the operation was successful or not.
     */
    public Boolean isSuccessful() {
        boolean success = this.resultType == ResultType.SUCCESS || this.resultType == ResultType.FOUND || this.resultType == ResultType.ALREADY_EXISTS;
        boolean isNull = this.result == null;

        return success && !isNull;
    }

}

