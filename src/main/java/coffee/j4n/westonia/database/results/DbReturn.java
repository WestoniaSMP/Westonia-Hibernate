package coffee.j4n.westonia.database.results;

import coffee.j4n.westonia.utils.statics.enums.ResultType;

/**
 * Represents the result of a database operation.
 */
public class DbReturn {

    /**
     * The message of the result (e.g. an error message).
     */
    private final String message;

    /**
     * The type of the result.
     */
    private final ResultType resultType;

    /**
     * Creates a new instance of the DbReturn
     *
     * @param message The message of the result.
     * @param resultType The type of the result.
     */
    public DbReturn(String message, ResultType resultType) {
        this.message = message;
        this.resultType = resultType;
    }

    /**
     * Returns the message of the result.
     *
     * @return The message of the result.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the type of the result.
     *
     * @return The type of the result.
     */
    public ResultType getResultType() {
        return resultType;
    }

    /**
     * Returns whether the operation was successful.
     *
     * @return Whether the operation was successful.
     */
    public Boolean isSuccessful() {
        return this.resultType == ResultType.SUCCESS;
    }

}
