package ernest;

/**
 * Represents a task that occurs within a time range.
 */
public class Event extends Task {
    protected String durationStart;
    protected String durationEnd;

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

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + durationStart + " to: " + durationEnd + ")";
    }
}
