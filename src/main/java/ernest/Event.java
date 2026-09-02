package ernest;

/**
 * Represents a task that occurs within a time range.
 */
public class Event extends Task {
    protected String duration_start;
    protected String duration_end;

    /**
     * Creates an event task.
     *
     * @param description description of the task.
     * @param duration_start start of the event.
     * @param duration_end end of the event.
     */
    public Event(String description, String duration_start, String duration_end) {
        super(description);
        this.duration_start = duration_start;
        this.duration_end = duration_end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + duration_start + " to: " + duration_end + ")";
    }
}
