package coffee.j4n.westonia.utils.messages;


import com.google.common.base.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeParser {

    /**
     * Converts a formatted time string (e.g., "1m", "2h", "2h3m55s", "22t5h3m55s") into seconds.
     *
     * @param timeString The formatted time string to convert.
     * @return An Optional containing the time in seconds if the string was successfully parsed, or an absent Optional otherwise.
     */
    public static Optional<Long> parseTimeToSeconds(String timeString) {
        // Regular expression to match time units: days (t), hours (h), minutes (m), seconds (s)
        Pattern pattern = Pattern.compile("(?:(\\d+)t)?(?:(\\d+)h)?(?:(\\d+)m)?(?:(\\d+)s)?");
        Matcher matcher = pattern.matcher(timeString);

        if (!matcher.matches()) {
            return Optional.absent();
        }

        // Parse time units or set to 0 if not present
        long days = matcher.group(1) != null ? Long.parseLong(matcher.group(1)) : 0;
        long hours = matcher.group(2) != null ? Long.parseLong(matcher.group(2)) : 0;
        long minutes = matcher.group(3) != null ? Long.parseLong(matcher.group(3)) : 0;
        long seconds = matcher.group(4) != null ? Long.parseLong(matcher.group(4)) : 0;


        // Convert all time units to seconds
        return Optional.of(days * 86400 + hours * 3600 + minutes * 60 + seconds);
    }

}
