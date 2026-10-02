package ernest.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a required task date with an optional time.
 */
public final class TaskDateTime {
    /** Format used to show dates to users. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
    /** Format used to show dates and times to users. */
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a", Locale.ENGLISH);

    /** Required calendar date. */
    private final LocalDate date;
    /** Optional time of day, or {@code null} for a date-only value. */
    private final LocalTime time;

    /**
     * Creates a date-only task value.
     *
     * @param date calendar date.
     */
    public TaskDateTime(LocalDate date) {
        this.date = Objects.requireNonNull(date);
        this.time = null;
    }

    /**
     * Creates a task value containing a date and time.
     *
     * @param dateTime calendar date and time.
     */
    public TaskDateTime(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime);
        this.date = dateTime.toLocalDate();
        this.time = dateTime.toLocalTime();
    }

    /**
     * Returns this value in the ISO format used by storage.
     *
     * @return ISO date or date-time text.
     */
    public String toStorageString() {
        return time == null ? date.toString() : LocalDateTime.of(date, time).toString();
    }

    @Override
    public String toString() {
        return time == null
                ? date.format(DISPLAY_DATE_FORMAT)
                : LocalDateTime.of(date, time).format(DISPLAY_DATE_TIME_FORMAT);
    }
}
