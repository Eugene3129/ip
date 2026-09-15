package ernest;

/**
 * Represents a task that occurs within a time range.
 */
public class Event extends Task {
    /** Start of the event. */
    private final String durationStart;
    /** End of the event. */
    private final String durationEnd;

    /**
     * Creates an event task.
     *
     * @param description description of the task.
     * @param durationStart start of the event.
     * @param durationEnd end of the event.
     */
    public Event(String description, String durationStart, String durationEnd) {
        super(description);
        this.durationStart = durationStart;
        this.durationEnd = durationEnd;
    }

    /**
     * Returns this event's start time.
     *
     * @return start time.
     */
    public String getDurationStart() {
        return this.durationStart;
    }

    /**
     * Returns this event's end time.
     *
     * @return end time.
     */
    public String getDurationEnd() {
        return this.durationEnd;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + durationStart + " to: " + durationEnd + ")";
    }
}
